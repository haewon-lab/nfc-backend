package haewon.nfc.server.dto;

public record ChargeResponse(
        boolean success, // 충전 성공 여부
        String cardId, // 카드 ID
        int chargedAmount, // 실제 충전된 금액
        int balance, // 충전 후 잔액
        String message // 결과 메시지
) {
}