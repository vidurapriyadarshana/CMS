package edu.epic.cms.service.impl;

import edu.epic.cms.exception.CardCreationException;
import edu.epic.cms.model.CardRequest;
import edu.epic.cms.repository.CardRequestRepo;
import edu.epic.cms.service.CardRequestService;
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
            throw new CardCreationException("Card number is required");
        }

        if (cardRequestRepo.hasPendingRequest(cardRequest.getCardNumber())) {
            throw new CardCreationException("A pending request already exists for this card number");
        }

        return cardRequestRepo.createCardRequest(cardRequest);
    }

    @Override
    public boolean updateStatus(String encryptedCardNumber, String status) {
        if (!cardRequestRepo.hasPendingRequest(encryptedCardNumber)) {
            throw new CardCreationException("No pending request found for this card number");
        }
        return cardRequestRepo.updateStatusByCardNumber(encryptedCardNumber, status);
    }
}
