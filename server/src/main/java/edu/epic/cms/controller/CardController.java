package edu.epic.cms.controller;

import edu.epic.cms.exception.CardCreationException;
import edu.epic.cms.exception.CardNotFoundException;
import edu.epic.cms.exception.CardException;
import edu.epic.cms.model.Card;
import edu.epic.cms.model.CardResponse;
import edu.epic.cms.model.UpdateCard;
import edu.epic.cms.service.CardService;
import edu.epic.cms.util.CommonResponse;
import jakarta.validation.Valid;
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

    @GetMapping("/{encryptedCardNumber}")
    public ResponseEntity<CommonResponse> getCardByEncryptedNumber(@PathVariable String encryptedCardNumber) {
        CardResponse card = cardService.getCardByEncryptedNumber(encryptedCardNumber);
        return ResponseEntity.ok(CommonResponse.success(card));
    }

    @DeleteMapping("/{encryptedCardNumber}")
    public ResponseEntity<CommonResponse> deleteCard(@PathVariable String encryptedCardNumber) {
        boolean isDeleted = cardService.deleteCard(encryptedCardNumber);
        if (isDeleted) {
            return ResponseEntity.ok(CommonResponse.success("Card deleted successfully"));
        }
        throw new CardException("Failed to delete card");
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createCard(@Valid @RequestBody Card card) {
        boolean isCreated = cardService.createCard(card);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.created("Card created successfully"));
        }
        throw new CardCreationException("Failed to create card");
    }

    @PutMapping("/{encryptedCardNumber}")
    public ResponseEntity<CommonResponse> updateCard(
            @PathVariable String encryptedCardNumber,
            @Valid @RequestBody UpdateCard updateCard) {
        boolean isUpdated = cardService.updateCard(encryptedCardNumber, updateCard);
        if (isUpdated) {
            return ResponseEntity.ok(CommonResponse.success("Card updated successfully"));
        }
        throw new CardException("Failed to update card");
    }
}
