package haewon.nfc.server.dto;

public record HwResponse(
        boolean success,
        String cardId,
        int fare,
        int balance,
        String message
) {}