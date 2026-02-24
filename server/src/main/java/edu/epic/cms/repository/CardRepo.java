package edu.epic.cms.repository;

import edu.epic.cms.model.Card;
import edu.epic.cms.api.UpdateCard;

import java.util.List;


public interface CardRepo {
    List<Card> getAllCards();
    boolean createCard(Card card);
    boolean existsByCardNumber(String cardNumber);
    boolean updateCard(String cardNumber, UpdateCard updateCard);
    Card getCardByNumber(String cardNumber);
    boolean deleteCard(String cardNumber);
}
