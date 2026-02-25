package edu.epic.cms.search.controller;

import edu.epic.cms.dto.CardResponseDTO;
import edu.epic.cms.dto.CommonResponseDTO;
import edu.epic.cms.search.service.CardSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cards/search")
public class CardSearchController {

    private final CardSearchService cardSearchService;

    public CardSearchController(CardSearchService cardSearchService) {
        this.cardSearchService = cardSearchService;
    }

    @GetMapping
    public ResponseEntity<CommonResponseDTO> searchCards(
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) String cardStatus) {
        List<CardResponseDTO> results = cardSearchService.searchCards(cardNumber, cardStatus);
        return ResponseEntity.ok(CommonResponseDTO.success(results));
    }
}
