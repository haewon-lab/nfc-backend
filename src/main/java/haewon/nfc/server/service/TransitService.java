package haewon.nfc.server.service;

import haewon.nfc.server.dto.HwResponse;
import haewon.nfc.server.dto.TransitHistoryResponse;
import haewon.nfc.server.dto.ChargeResponse;
import haewon.nfc.server.entity.TransitCard;
import haewon.nfc.server.entity.TransitTransaction;
import haewon.nfc.server.repository.TransitCardRepository;
import haewon.nfc.server.repository.TransitTransactionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransitService {
    private static final String DEMO_CARD_ID = "TOUCHON-0001";
    private static final int INITIAL_BALANCE = 10000;
    private static final int FARE = 1500;
    private static final long DUPLICATE_INTERVAL_MS = 3000;

    private final TransitCardRepository cardRepository;
    private final TransitTransactionRepository transactionRepository;

    public TransitService(
            TransitCardRepository cardRepository,
            TransitTransactionRepository transactionRepository
    ) {
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    @PostConstruct
    public void initializeDemoCard() {
        if (!cardRepository.existsById(DEMO_CARD_ID)) {
            cardRepository.save(
                    new TransitCard(DEMO_CARD_ID, INITIAL_BALANCE)
            );
        }
    }

    @Transactional
    public synchronized HwResponse tap(String cardId) {

        TransitCard card = cardRepository.findById(cardId).orElse(null);

        if (card == null) {
            return record(
                    cardId, false, 0, 0,
                    "등록되지 않은 카드입니다."
            );
        }

        long now = System.currentTimeMillis();

        if (card.getLastTapTime() > 0
                && now - card.getLastTapTime() < DUPLICATE_INTERVAL_MS) {
            return record(
                    cardId, false, 0, card.getBalance(),
                    "중복 태그입니다. 잠시 후 다시 시도해주세요."
            );
        }

        if (card.getBalance() < FARE) {
            return record(
                    cardId, false, 0, card.getBalance(),
                    "잔액이 부족합니다."
            );
        }

        card.setBalance(card.getBalance() - FARE);
        card.setLastTapTime(now);

        cardRepository.save(card);

        return record(
                cardId, true, FARE, card.getBalance(),
                "승차 처리 완료"
        );
    }

    private HwResponse record(
            String cardId,
            boolean success,
            int fare,
            int balance,
            String message
    ) {
        TransitTransaction transaction = new TransitTransaction(
                cardId,
                "TAP",
                LocalDateTime.now(),
                fare,
                balance,
                success,
                message
        );

        transactionRepository.save(transaction);

        return new HwResponse(
                success, cardId, fare, balance, message
        );
    }

    @Transactional(readOnly = true)
    public List<TransitHistoryResponse> getHistory() {

        return transactionRepository
                .findAllByOrderByTransactionIdDesc()
                .stream()
                .map(transaction -> new TransitHistoryResponse(
                        transaction.getTransactionId(),
                        transaction.getCardId(),
                        transaction.getTransactionType(),
                        transaction.getTimestamp(),
                        transaction.getFare(),
                        transaction.getBalance(),
                        transaction.isSuccess(),
                        transaction.getMessage()
                ))
                .toList();
    }

    @Transactional
    public synchronized ChargeResponse charge(String cardId, int amount) {

        // 충전 금액 검증
        if (amount <= 0) {
            return new ChargeResponse(
                    false, cardId, 0, 0,
                    "충전 금액은 1원 이상이어야 합니다."
            );
        }

        // 카드 조회
        TransitCard card = cardRepository.findById(cardId).orElse(null);

        if (card == null) {
            return new ChargeResponse(
                    false, cardId, 0, 0,
                    "등록되지 않은 카드입니다."
            );
        }

        // 잔액 오버플로 방지
        if (card.getBalance() > Integer.MAX_VALUE - amount) {
            return new ChargeResponse(
                    false, cardId, 0, card.getBalance(),
                    "충전 가능한 잔액 범위를 초과했습니다."
            );
        }

        // 잔액 충전
        int newBalance = card.getBalance() + amount;

        card.setBalance(newBalance);
        cardRepository.save(card);

        TransitTransaction transaction = new TransitTransaction(
                cardId,
                "CHARGE",
                LocalDateTime.now(),
                amount,
                newBalance,
                true,
                "충전 완료"
        );
        transactionRepository.save(transaction);

        return new ChargeResponse(
                true,
                cardId,
                amount,
                newBalance,
                "충전 완료"
        );
    }
}
