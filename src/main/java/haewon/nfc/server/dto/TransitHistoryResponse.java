package haewon.nfc.server.dto;

import java.time.LocalDateTime;

public record TransitHistoryResponse(
        long transactionId, // 거래 고유 번호
        String cardId, // 카드 ID
        String transactionType, // 거래 타입
        LocalDateTime timestamp, // 거래 처리 시각
        int amount, // 실제 차감 요금
        int balance, // 처리 후 잔액
        boolean success, // 승차 승인 여부
        String message // 처리 결과 메시지
) {
}
