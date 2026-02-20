package edu.epic.cms.controller;

import edu.epic.cms.exception.CardException;
import edu.epic.cms.model.CardRequest;
import edu.epic.cms.api.StatusUpdateRequest;
import edu.epic.cms.service.CardRequestService;
import edu.epic.cms.api.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card-requests")
public class CardRequestController {

    private final CardRequestService cardRequestService;

    public CardRequestController(CardRequestService cardRequestService) {
        this.cardRequestService = cardRequestService;
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAllCardRequests() {
        List<CardRequest> requests = cardRequestService.getAllCardRequests();
        return ResponseEntity.ok(CommonResponse.success(requests));
    }

    @GetMapping("/{encryptedCardNumber}")
    public ResponseEntity<CommonResponse> getCardRequestsByCardNumber(@PathVariable String encryptedCardNumber) {
        List<CardRequest> requests = cardRequestService.getCardRequestsByCardNumber(encryptedCardNumber);
        return ResponseEntity.ok(CommonResponse.success(requests));
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createCardRequest(@Valid @RequestBody CardRequest cardRequest) {
        boolean isCreated = cardRequestService.createCardRequest(cardRequest);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.created("Card request created successfully"));
        }
        throw new CardException("Failed to create card request");
    }

    @PutMapping("/{encryptedCardNumber}/status")
    public ResponseEntity<CommonResponse> updateStatus(
            @PathVariable String encryptedCardNumber,
            @Valid @RequestBody StatusUpdateRequest statusUpdateRequest) {
        boolean isUpdated = cardRequestService.updateStatus(encryptedCardNumber, statusUpdateRequest.getStatus());
        if (isUpdated) {
            return ResponseEntity.ok(CommonResponse.success("Card request status updated successfully"));
        }
        throw new CardException("Failed to update card request status");
    }
}
