package haewon.nfc.server.controller;

import haewon.nfc.server.dto.*;
import haewon.nfc.server.service.TransitService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transit")
public class TransitController {
    private final TransitService transitService;

    public TransitController(TransitService transitService) {
        this.transitService = transitService;
    }

    @PostMapping("/tap")
    public HwResponse tap(
            @Valid @RequestBody HwRequest request
    ) {
        return transitService.tap(request.cardId());
    }

    @GetMapping("/history")
    public List<TransitHistoryResponse> history() {
        return transitService.getHistory();
    }

    @PostMapping("/charge")
    public ChargeResponse charge(
            @Valid @RequestBody ChargeRequest request
    ) {
        return transitService.charge(
                request.cardId(),
                request.amount()
        );
    }

    @GetMapping("/cards/{cardId}")
    public CardBalanceResponse getCardBalance(
            @PathVariable String cardId
    ) {
        return transitService.getCardBalance(cardId);
    }
}
