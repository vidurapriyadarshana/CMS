package edu.epic.cms.service.impl;

import edu.epic.cms.exception.CardCreationException;
import edu.epic.cms.exception.CardNotFoundException;
import edu.epic.cms.exception.DuplicateCardException;
import edu.epic.cms.exception.OutstandingBalanceException;
import edu.epic.cms.model.Card;
import edu.epic.cms.api.CardResponseDTO;
import edu.epic.cms.api.UpdateCardDTO;
import edu.epic.cms.repository.CardRepo;

import edu.epic.cms.service.CardService;
import edu.epic.cms.util.CardEncryptionUtil;
import edu.epic.cms.util.RsaEncryptionUtil;
import edu.epic.cms.repository.CardRequestRepo;
import edu.epic.cms.api.CreateCardRequestDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepo cardRepo;
    private final CardRequestRepo cardRequestRepo;
    private final RsaEncryptionUtil rsaEncryptionUtil;

    public CardServiceImpl(CardRepo cardRepo, CardRequestRepo cardRequestRepo, RsaEncryptionUtil rsaEncryptionUtil) {
        this.cardRepo = cardRepo;
        this.cardRequestRepo = cardRequestRepo;
        this.rsaEncryptionUtil = rsaEncryptionUtil;
    }

    @Override
    public List<CardResponseDTO> getAllCards() {
        List<Card> cards = cardRepo.getAllCards();
        List<CardResponseDTO> cardResponses = new ArrayList<>();

        for (Card card : cards) {
            CardResponseDTO response = new CardResponseDTO();

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
            response.setLastUpdatedUser(card.getLastUpdatedUser());
            cardResponses.add(response);

        }

        return cardResponses;
    }

    @Override
    public boolean createCard(CreateCardRequestDTO request) {
        if (request.getCardNumber() == null || request.getCardNumber().isEmpty()) {
            throw new CardCreationException("Card number is required");
        }

        String decryptedCardNumber;
        try {
            decryptedCardNumber = rsaEncryptionUtil.decrypt(request.getCardNumber());
        } catch (Exception e) {
            throw new CardCreationException("Invalid or malformed card encrypted payload");
        }

        if (decryptedCardNumber.length() != 16 || !decryptedCardNumber.matches("^[0-9]{16}$")) {
            throw new CardCreationException("Decrypted card number must be exactly 16 digits");
        }

        if (request.getCreditLimit() == null || request.getCreditLimit() < 0) {
            throw new CardCreationException("Credit limit must be greater than or equal to 0");
        }

        if (request.getCashLimit() == null || request.getCashLimit() < 0) {
            throw new CardCreationException("Cash limit must be greater than or equal to 0");
        }

        if (request.getCashLimit() > request.getCreditLimit()) {
            throw new CardCreationException("Cash limit cannot exceed credit limit");
        }

        Card card = new Card();
        card.setExpireDate(request.getExpireDate());
        card.setCreditLimit(request.getCreditLimit());
        card.setCashLimit(request.getCashLimit());
        card.setAvailableCreditLimit(request.getCreditLimit());
        card.setAvailableCashLimit(request.getCashLimit());
        card.setLastUpdatedUser(request.getLastUpdatedUser());


        String encryptedCardNumber = CardEncryptionUtil.encrypt(decryptedCardNumber);
        
        if (cardRepo.existsByCardNumber(encryptedCardNumber)) {
            throw new DuplicateCardException("Card with number " + CardEncryptionUtil.maskCardNumber(decryptedCardNumber) + " already exists");
        }

        card.setCardNumber(encryptedCardNumber);
        
        return cardRepo.createCard(card);
    }

    @Override
    public boolean updateCard(String encryptedCardNumber, UpdateCardDTO updateCard) {
        if (!cardRepo.existsByCardNumber(encryptedCardNumber)) {
            throw new CardNotFoundException("Card not found");
        }

        if (cardRequestRepo.isCardDeactivated(encryptedCardNumber)) {
            throw new CardCreationException("Cannot update card: Card is deactivated");
        }

        // Removed pending request validation to allow direct updates

        if (updateCard.getCashLimit() > updateCard.getCreditLimit()) {
            throw new CardCreationException("Cash limit cannot exceed credit limit");
        }

        if (updateCard.getAvailableCreditLimit() > updateCard.getCreditLimit()) {
            throw new CardCreationException("Available credit limit cannot exceed credit limit");
        }

        if (updateCard.getAvailableCashLimit() > updateCard.getCashLimit()) {
            throw new CardCreationException("Available cash limit cannot exceed cash limit");
        }

        if (updateCard.getAvailableCashLimit() > updateCard.getAvailableCreditLimit()) {
            throw new CardCreationException("Available cash limit cannot exceed available credit limit");
        }

        return cardRepo.updateCard(encryptedCardNumber, updateCard);
    }

    @Override
    public CardResponseDTO getCardByEncryptedNumber(String encryptedCardNumber) {
        Card card = cardRepo.getCardByNumber(encryptedCardNumber);
        if (card == null) {
            throw new CardNotFoundException("Card not found");
        }

        CardResponseDTO response = new CardResponseDTO();
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
        response.setLastUpdatedUser(card.getLastUpdatedUser());

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

        if (!cardRequestRepo.hasPendingRequest(encryptedCardNumber)) {
            throw new CardCreationException("No pending request found for this card number");
        }

        Card card = cardRepo.getCardByNumber(encryptedCardNumber);
        if (card != null) {
            if (!card.getCreditLimit().equals(card.getAvailableCreditLimit())) {
                cardRequestRepo.markRequestAsFailed(encryptedCardNumber, card.getLastUpdatedUser() != null ? card.getLastUpdatedUser() : "SYSTEM");
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
