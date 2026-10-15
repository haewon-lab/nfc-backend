package haewon.nfc.server.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChargeRequest(
        @NotBlank String cardId,
        @NotNull @Min(1) Integer amount
) {
}