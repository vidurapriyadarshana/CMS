package edu.epic.cms.search.repository;

import edu.epic.cms.model.Card;
import java.util.List;

public interface CardSearchRepository {
    List<Card> searchCards(String encryptedCardNumber, String cardStatus);
}
