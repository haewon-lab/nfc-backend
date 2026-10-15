package haewon.nfc.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "transit_transactions")
public class TransitTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId; // 자동 생성되는 거래 번호

    private String cardId; // 태그한 카드 ID

    private LocalDateTime timestamp; // 처리 시각

    private int fare; // 차감된 요금

    private int balance; // 처리 후 잔액

    private boolean success; // 승차 성공 여부

    private String message; // 승차 처리 결과 메시지

    protected TransitTransaction() {
    }

    public TransitTransaction(
            String cardId,
            LocalDateTime timestamp,
            int fare,
            int balance,
            boolean success,
            String message
    ) {
        this.cardId = cardId;
        this.timestamp = timestamp;
        this.fare = fare;
        this.balance = balance;
        this.success = success;
        this.message = message;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public String getCardId() {
        return cardId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getFare() {
        return fare;
    }

    public int getBalance() {
        return balance;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}