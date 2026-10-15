package haewon.nfc.server.service;

import haewon.nfc.server.dto.HwResponse;
import haewon.nfc.server.dto.TransitHistoryResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransitService {
    private static final String DEMO_CARD_ID = "TOUCHON-0001";
    private static final int FARE = 1500;
    private static final long DUPLICATE_INTERVAL_MS = 3000;

    private int balance = 10000;
    private long lastTapTime = 0;
    private long nextTransactionId = 1;

    private final List<TransitHistoryResponse> history =
            new ArrayList<>();

    public synchronized HwResponse tap(String cardId) {

        // 1. 등록되지 않은 카드
        if (!DEMO_CARD_ID.equals(cardId)) {
            return record(
                    cardId,
                    false,
                    0,
                    0,
                    "등록되지 않은 카드입니다."
            );
        }

        long now = System.currentTimeMillis();

        // 2. 중복 승차 방지
        if (lastTapTime > 0 &&
                now - lastTapTime < DUPLICATE_INTERVAL_MS) {

            return record(
                    cardId,
                    false,
                    0,
                    balance,
                    "중복 태그입니다. 잠시 후 다시 시도해주세요."
            );
        }

        // 3. 잔액 부족
        if (balance < FARE) {
            return record(
                    cardId,
                    false,
                    0,
                    balance,
                    "잔액이 부족합니다."
            );
        }

        // 4. 정상 승차
        balance -= FARE;
        lastTapTime = now;

        return record(
                cardId,
                true,
                FARE,
                balance,
                "승차 처리 완료"
        );
    }

    // 거래 결과 기록 및 기존 API 응답 생성
    private HwResponse record(
            String cardId,
            boolean success,
            int fare,
            int currentBalance,
            String message
    ) {
        TransitHistoryResponse transaction =
                new TransitHistoryResponse(
                        nextTransactionId++,
                        cardId,
                        LocalDateTime.now(),
                        fare,
                        currentBalance,
                        success,
                        message
                );

        history.add(transaction);

        return new HwResponse(
                success,
                cardId,
                fare,
                currentBalance,
                message
        );
    }

    // 최근 거래부터 반환
    public synchronized List<TransitHistoryResponse> getHistory() {
        List<TransitHistoryResponse> result =
                new ArrayList<>(history);

        java.util.Collections.reverse(result);

        return result;
    }
}
