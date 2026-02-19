package edu.epic.cms.service;

import edu.epic.cms.model.Card;
import edu.epic.cms.api.CardResponse;
import edu.epic.cms.api.UpdateCard;


import java.util.List;

public interface CardService {
    List<CardResponse> getAllCards();
    boolean createCard(Card card);
    boolean updateCard(String encryptedCardNumber, UpdateCard updateCard);
    CardResponse getCardByEncryptedNumber(String encryptedCardNumber);
    boolean deleteCard(String encryptedCardNumber);
}

