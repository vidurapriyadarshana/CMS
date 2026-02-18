package edu.epic.cms.controller;

import edu.epic.cms.model.Card;
import edu.epic.cms.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        List<Card> cards = cardService.getAllCards();
        return ResponseEntity.ok(cards);
    }
}
