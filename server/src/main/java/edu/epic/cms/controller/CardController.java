package edu.epic.cms.controller;

import edu.epic.cms.exception.CardException;
import edu.epic.cms.dto.CardResponseDTO;
import edu.epic.cms.dto.CreateCardRequestDTO;
import edu.epic.cms.dto.UpdateCardDTO;
import edu.epic.cms.service.CardService;
import edu.epic.cms.dto.CommonResponseDTO;

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
    public ResponseEntity<CommonResponseDTO> getAllCards(@RequestParam(required = false) String cardStatus) {
        List<CardResponseDTO> cards = cardService.getAllCards(cardStatus);
        return ResponseEntity.ok(CommonResponseDTO.success(cards != null ? cards : List.of()));
    }

    @GetMapping("/{encryptedCardNumber}")
    public ResponseEntity<CommonResponseDTO> getCardByEncryptedNumber(@PathVariable String encryptedCardNumber) {
        CardResponseDTO card = cardService.getCardByEncryptedNumber(encryptedCardNumber);
        return ResponseEntity.ok(CommonResponseDTO.success(card));
    }

    @DeleteMapping("/{encryptedCardNumber}")
    public ResponseEntity<CommonResponseDTO> deleteCard(@PathVariable String encryptedCardNumber) {
        boolean isDeleted = cardService.deleteCard(encryptedCardNumber);
        if (isDeleted) {
            return ResponseEntity.ok(CommonResponseDTO.success("Card deleted successfully"));
        }
        throw new CardException("Failed to delete card");
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> createCard(@Valid @RequestBody CreateCardRequestDTO createCardRequest) {
        boolean isCreated = cardService.createCard(createCardRequest);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponseDTO.created("Card created successfully"));
        }
        throw new CardException("Failed to create card");
    }

    @PutMapping("/{encryptedCardNumber}")
    public ResponseEntity<CommonResponseDTO> updateCard(
            @PathVariable String encryptedCardNumber,
            @Valid @RequestBody UpdateCardDTO updateCard) {
        boolean isUpdated = cardService.updateCard(encryptedCardNumber, updateCard);
        if (isUpdated) {
            return ResponseEntity.ok(CommonResponseDTO.success("Card updated successfully"));
        }
        throw new CardException("Failed to update card");
    }
}
