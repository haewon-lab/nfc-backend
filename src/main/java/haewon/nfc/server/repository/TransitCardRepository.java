package haewon.nfc.server.repository;

import haewon.nfc.server.entity.TransitCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransitCardRepository
        extends JpaRepository<TransitCard, String> {
}