package edu.epic.cms.controller;

import edu.epic.cms.exception.CardCreationException;
import edu.epic.cms.exception.CardNotFoundException;
import edu.epic.cms.model.Card;
import edu.epic.cms.model.CardResponse;
import edu.epic.cms.service.CardService;
import edu.epic.cms.util.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAllCards() {
        List<CardResponse> cards = cardService.getAllCards();
        if (cards == null || cards.isEmpty()) {
            throw new CardNotFoundException("No cards found");
        }
        return ResponseEntity.ok(CommonResponse.success(cards));
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createCard(@RequestBody Card card) {
        boolean isCreated = cardService.createCard(card);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.created("Card created successfully"));
        }
        throw new CardCreationException("Failed to create card");
    }
}
