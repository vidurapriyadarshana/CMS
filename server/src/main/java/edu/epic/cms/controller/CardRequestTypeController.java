package edu.epic.cms.controller;

import edu.epic.cms.api.CommonResponse;
import edu.epic.cms.model.CardRequestType;
import edu.epic.cms.service.CardRequestTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/card-request-types")
public class CardRequestTypeController {

    private final CardRequestTypeService cardRequestTypeService;

    public CardRequestTypeController(CardRequestTypeService cardRequestTypeService) {
        this.cardRequestTypeService = cardRequestTypeService;
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAllCardRequestTypes() {
        List<CardRequestType> types = cardRequestTypeService.getAllCardRequestTypes();
        return ResponseEntity.ok(CommonResponse.success(types));
    }
}
