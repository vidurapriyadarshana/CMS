package edu.epic.cms.service.impl;

import edu.epic.cms.exception.CardCreationException;
import edu.epic.cms.exception.OutstandingBalanceException;
import edu.epic.cms.model.Card;
import edu.epic.cms.model.CardRequest;
import edu.epic.cms.repository.CardRepo;
import edu.epic.cms.repository.CardRequestRepo;
import edu.epic.cms.service.CardRequestService;
import org.springframework.stereotype.Service;

@Service
public class CardRequestServiceImpl implements CardRequestService {

    private final CardRequestRepo cardRequestRepo;
    private final CardRepo cardRepo;

    public CardRequestServiceImpl(CardRequestRepo cardRequestRepo, CardRepo cardRepo) {
        this.cardRequestRepo = cardRequestRepo;
        this.cardRepo = cardRepo;
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
        if (cardRequestRepo.isCardDeactivated(encryptedCardNumber)) {
            throw new CardCreationException("Card is deactivated");
        }

        if (!cardRequestRepo.hasPendingRequest(encryptedCardNumber)) {
            throw new CardCreationException("No pending request found for this card number");
        }

        if ("DACT".equals(status)) {
            Card card = cardRepo.getCardByNumber(encryptedCardNumber);
            if (card != null) {
                if (!card.getCreditLimit().equals(card.getAvailableCreditLimit())) {
                    cardRequestRepo.markRequestAsFailed(encryptedCardNumber);
                    throw new OutstandingBalanceException("Cannot deactivate card: outstanding balance exists");
                }
            }
        }

        return cardRequestRepo.updateStatusByCardNumber(encryptedCardNumber, status);
    }
}
