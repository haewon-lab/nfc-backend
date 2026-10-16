package haewon.nfc.server.dto;

public record CardBalanceResponse(
        String cardId, // 조회한 카드 ID
        int balance // H2 DB에 저장된 현재 잔액
) {
}