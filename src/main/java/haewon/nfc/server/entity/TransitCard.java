package haewon.nfc.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transit_cards")
public class TransitCard {
    @Id
    private String cardId;  // 카드 고유 ID

    private int balance; // 현재 카드 잔액

    private long lastTapTime; // 마지막 정상 승차 시각 (밀리초)

    protected TransitCard() {
    }

    public TransitCard(String cardId, int balance) {
        this.cardId = cardId;
        this.balance = balance;
        this.lastTapTime = 0;
    }

    public String getCardId() {
        return cardId;
    }

    public int getBalance() {
        return balance;
    }

    public long getLastTapTime() {
        return lastTapTime;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setLastTapTime(long lastTapTime) {
        this.lastTapTime = lastTapTime;
    }
}