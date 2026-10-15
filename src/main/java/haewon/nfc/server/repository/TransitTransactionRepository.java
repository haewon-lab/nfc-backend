package haewon.nfc.server.repository;

import haewon.nfc.server.entity.TransitTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransitTransactionRepository
        extends JpaRepository<TransitTransaction, Long> {

    List<TransitTransaction> findAllByOrderByTransactionIdDesc();
}