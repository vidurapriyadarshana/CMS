package edu.epic.cms.repository;

import edu.epic.cms.model.Card;
import edu.epic.cms.api.UpdateCardDTO;

import java.util.List;


public interface CardRepo {
    List<Card> getAllCards(String cardStatus);
    boolean createCard(Card card);
    boolean existsByCardNumber(String cardNumber);
    boolean updateCard(String cardNumber, UpdateCardDTO updateCard);
    Card getCardByNumber(String cardNumber);
    boolean deleteCard(String cardNumber);
}
