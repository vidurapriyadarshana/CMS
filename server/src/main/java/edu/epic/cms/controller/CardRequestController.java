package edu.epic.cms.controller;

import edu.epic.cms.exception.CardException;
import edu.epic.cms.model.CardRequest;
import edu.epic.cms.service.CardRequestService;
import edu.epic.cms.util.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card-requests")
public class CardRequestController {

    private final CardRequestService cardRequestService;

    public CardRequestController(CardRequestService cardRequestService) {
        this.cardRequestService = cardRequestService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createCardRequest(@Valid @RequestBody CardRequest cardRequest) {
        boolean isCreated = cardRequestService.createCardRequest(cardRequest);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.created("Card request created successfully"));
        }
        throw new CardException("Failed to create card request");
    }
}

