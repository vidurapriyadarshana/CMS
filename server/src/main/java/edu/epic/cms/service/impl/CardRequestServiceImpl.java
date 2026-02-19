package edu.epic.cms.service.impl;

import edu.epic.cms.exception.CardException;
import edu.epic.cms.model.CardRequest;
import edu.epic.cms.repository.CardRequestRepo;
import edu.epic.cms.service.CardRequestService;
import edu.epic.cms.util.CardEncryptionUtil;
import org.springframework.stereotype.Service;

@Service
public class CardRequestServiceImpl implements CardRequestService {

    private final CardRequestRepo cardRequestRepo;

    public CardRequestServiceImpl(CardRequestRepo cardRequestRepo) {
        this.cardRequestRepo = cardRequestRepo;
    }

    @Override
    public boolean createCardRequest(CardRequest cardRequest) {
        if (cardRequest.getCardNumber() == null || cardRequest.getCardNumber().isEmpty()) {
            throw new CardException("Card number is required");
        }

        return cardRequestRepo.createCardRequest(cardRequest);
    }
}

