package edu.epic.cms.service.impl;

import edu.epic.cms.exception.CardCreationException;
import edu.epic.cms.exception.CardNotFoundException;
import edu.epic.cms.exception.DuplicateCardException;
import edu.epic.cms.exception.OutstandingBalanceException;
import edu.epic.cms.model.Card;
import edu.epic.cms.api.CardResponse;
import edu.epic.cms.api.UpdateCard;
import edu.epic.cms.repository.CardRepo;

import edu.epic.cms.service.CardService;
import edu.epic.cms.util.CardEncryptionUtil;
import edu.epic.cms.repository.CardRequestRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepo cardRepo;
    private final CardRequestRepo cardRequestRepo;

    public CardServiceImpl(CardRepo cardRepo, CardRequestRepo cardRequestRepo) {
        this.cardRepo = cardRepo;
        this.cardRequestRepo = cardRequestRepo;
    }

    @Override
    public List<CardResponse> getAllCards() {
        List<Card> cards = cardRepo.getAllCards();
        List<CardResponse> cardResponses = new ArrayList<>();

        for (Card card : cards) {
            CardResponse response = new CardResponse();

            String encryptedCardNumber = card.getCardNumber();
            response.setEncryptedCardNumber(encryptedCardNumber);

            String decryptedCardNumber = CardEncryptionUtil.decrypt(encryptedCardNumber);

            String maskedCardNumber = CardEncryptionUtil.maskCardNumber(decryptedCardNumber);
            response.setCardNumber(maskedCardNumber);
            
            response.setExpireDate(card.getExpireDate());
            response.setCardStatus(card.getCardStatus());
            response.setCreditLimit(card.getCreditLimit());
            response.setCashLimit(card.getCashLimit());
            response.setAvailableCreditLimit(card.getAvailableCreditLimit());
            response.setAvailableCashLimit(card.getAvailableCashLimit());
            response.setLastUpdateTime(card.getLastUpdateTime());
            cardResponses.add(response);
        }

        return cardResponses;
    }

    @Override
    public boolean createCard(Card card) {
        if (card.getCardNumber() == null || card.getCardNumber().isEmpty()) {
            throw new CardCreationException("Card number is required");
        }

        String encryptedCardNumber = CardEncryptionUtil.encrypt(card.getCardNumber());
        
        if (cardRepo.existsByCardNumber(encryptedCardNumber)) {
            throw new DuplicateCardException("Card with number " + CardEncryptionUtil.maskCardNumber(card.getCardNumber()) + " already exists");
        }

        card.setCardNumber(encryptedCardNumber);
        
        return cardRepo.createCard(card);
    }

    @Override
    public boolean updateCard(String encryptedCardNumber, UpdateCard updateCard) {
        if (!cardRepo.existsByCardNumber(encryptedCardNumber)) {
            throw new CardNotFoundException("Card not found");
        }

        return cardRepo.updateCard(encryptedCardNumber, updateCard);
    }

    @Override
    public CardResponse getCardByEncryptedNumber(String encryptedCardNumber) {
        Card card = cardRepo.getCardByNumber(encryptedCardNumber);
        if (card == null) {
            throw new CardNotFoundException("Card not found");
        }

        CardResponse response = new CardResponse();
        response.setEncryptedCardNumber(card.getCardNumber());

        String decryptedCardNumber = CardEncryptionUtil.decrypt(card.getCardNumber());
        String maskedCardNumber = CardEncryptionUtil.maskCardNumber(decryptedCardNumber);
        response.setCardNumber(maskedCardNumber);

        response.setExpireDate(card.getExpireDate());
        response.setCardStatus(card.getCardStatus());
        response.setCreditLimit(card.getCreditLimit());
        response.setCashLimit(card.getCashLimit());
        response.setAvailableCreditLimit(card.getAvailableCreditLimit());
        response.setAvailableCashLimit(card.getAvailableCashLimit());
        response.setLastUpdateTime(card.getLastUpdateTime());

        return response;
    }

    @Override
    public boolean deleteCard(String encryptedCardNumber) {
        if (!cardRepo.existsByCardNumber(encryptedCardNumber)) {
            throw new CardNotFoundException("Card not found");
        }

        if (cardRequestRepo.isCardDeactivated(encryptedCardNumber)) {
            throw new CardCreationException("Card is deactivated");
        }

        Card card = cardRepo.getCardByNumber(encryptedCardNumber);
        if (card != null) {
            if (!card.getCreditLimit().equals(card.getAvailableCreditLimit())) {
                cardRequestRepo.markRequestAsFailed(encryptedCardNumber);
                throw new OutstandingBalanceException("Cannot deactivate card: outstanding balance exists");
            }
        }

        boolean deleted = cardRepo.deleteCard(encryptedCardNumber);
        if (deleted) {
            cardRequestRepo.markRequestAsDeactivated(encryptedCardNumber);
        }
        return deleted;
    }
}

