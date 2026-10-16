package haewon.nfc.server.repository;

import haewon.nfc.server.entity.TransitTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransitTransactionRepository
        extends JpaRepository<TransitTransaction, Long> {

    // 기존 전체 거래 내역 조회
    List<TransitTransaction> findAllByOrderByTransactionIdDesc();

    // 특정 카드의 거래 내역 조회
    List<TransitTransaction>
    findByCardIdOrderByTransactionIdDesc(String cardId);
}