package edu.epic.cms.service.impl;

import edu.epic.cms.exception.CardCreationException;
import edu.epic.cms.exception.OutstandingBalanceException;
import edu.epic.cms.model.Card;
import edu.epic.cms.model.CardRequest;
import edu.epic.cms.repository.CardRepo;
import edu.epic.cms.repository.CardRequestRepo;
import edu.epic.cms.service.CardRequestService;
import edu.epic.cms.util.CardEncryptionUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardRequestServiceImpl implements CardRequestService {

    private final CardRequestRepo cardRequestRepo;
    private final CardRepo cardRepo;

    public CardRequestServiceImpl(CardRequestRepo cardRequestRepo, CardRepo cardRepo) {
        this.cardRequestRepo = cardRequestRepo;
        this.cardRepo = cardRepo;
    }

    @Override
    public List<CardRequest> getAllCardRequests() {
        List<CardRequest> requests = cardRequestRepo.getAllCardRequests();
        maskCardNumbers(requests);
        return requests;
    }

    @Override
    public List<CardRequest> getCardRequestsByCardNumber(String encryptedCardNumber) {
        List<CardRequest> requests = cardRequestRepo.getCardRequestsByCardNumber(encryptedCardNumber);
        maskCardNumbers(requests);
        return requests;
    }

    private void maskCardNumbers(List<CardRequest> requests) {
        for (CardRequest request : requests) {
            String encrypted = request.getCardNumber();
            request.setEncryptedCardNumber(encrypted);
            String decrypted = CardEncryptionUtil.decrypt(encrypted);
            String masked = CardEncryptionUtil.maskCardNumber(decrypted);
            request.setCardNumber(masked);
        }
    }

    @Override
    public boolean createCardRequest(CardRequest cardRequest) {
        if (cardRequest.getCardNumber() == null || cardRequest.getCardNumber().isEmpty()) {
            throw new CardCreationException("Card number is required");
        }

        if (!cardRepo.existsByCardNumber(cardRequest.getCardNumber())) {
            throw new edu.epic.cms.exception.CardNotFoundException("Card not found");
        }

        if (cardRequestRepo.isCardDeactivated(cardRequest.getCardNumber())) {
            throw new CardCreationException("Cannot create request: Card is deactivated");
        }

        if (cardRequestRepo.hasPendingRequest(cardRequest.getCardNumber())) {
            throw new CardCreationException("A pending request already exists for this card number");
        }

        return cardRequestRepo.createCardRequest(cardRequest);
    }

    @Override
    public boolean updateStatus(String encryptedCardNumber, String status, String approvedUser) {
        if (!cardRepo.existsByCardNumber(encryptedCardNumber)) {
            throw new edu.epic.cms.exception.CardNotFoundException("Card not found");
        }

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
                    cardRequestRepo.markRequestAsFailed(encryptedCardNumber, approvedUser);
                    throw new OutstandingBalanceException("Cannot deactivate card: outstanding balance exists");
                }
            }
        }

        try {
            boolean result = cardRequestRepo.updateStatusByCardNumber(encryptedCardNumber, status, approvedUser, "COMPLETE");
            if (!result) {
                cardRequestRepo.markRequestAsFailed(encryptedCardNumber, approvedUser);
            }
            return result;
        } catch (Exception e) {
            cardRequestRepo.markRequestAsFailed(encryptedCardNumber, approvedUser);
            throw e;
        }
    }
}
