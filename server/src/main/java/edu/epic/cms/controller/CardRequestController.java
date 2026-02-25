package edu.epic.cms.controller;

import edu.epic.cms.exception.CardException;
import edu.epic.cms.model.CardRequest;
import edu.epic.cms.api.StatusUpdateRequestDTO;
import edu.epic.cms.service.CardRequestService;
import edu.epic.cms.api.CommonResponseDTO;
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
    public ResponseEntity<CommonResponseDTO> getAllCardRequests(
            @RequestParam(required = false) String requestReasonCode,
            @RequestParam(required = false) String requestStatus) {
        List<CardRequest> requests = cardRequestService.getAllCardRequests(requestReasonCode, requestStatus);
        return ResponseEntity.ok(CommonResponseDTO.success(requests));
    }

    @GetMapping("/{encryptedCardNumber}")
    public ResponseEntity<CommonResponseDTO> getCardRequestsByCardNumber(@PathVariable String encryptedCardNumber) {
        List<CardRequest> requests = cardRequestService.getCardRequestsByCardNumber(encryptedCardNumber);
        return ResponseEntity.ok(CommonResponseDTO.success(requests));
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> createCardRequest(@Valid @RequestBody CardRequest cardRequest) {
        boolean isCreated = cardRequestService.createCardRequest(cardRequest);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponseDTO.created("Card request created successfully"));
        }
        throw new CardException("Failed to create card request");
    }

    @PutMapping("/{encryptedCardNumber}/status")
    public ResponseEntity<CommonResponseDTO> updateStatus(
            @PathVariable String encryptedCardNumber,
            @Valid @RequestBody StatusUpdateRequestDTO statusUpdateRequest) {
        boolean isUpdated = cardRequestService.updateStatus(encryptedCardNumber, statusUpdateRequest.getStatus(), statusUpdateRequest.getApprovedUser());
        if (isUpdated) {
            return ResponseEntity.ok(CommonResponseDTO.success("Card request status updated successfully"));
        }
        throw new CardException("Failed to update card request status");
    }
}
