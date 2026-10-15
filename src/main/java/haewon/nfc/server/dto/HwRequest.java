package haewon.nfc.server.dto;

import jakarta.validation.constraints.NotBlank;

public record HwRequest(
        @NotBlank String cardId
) {}