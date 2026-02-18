package edu.epic.cms.service.impl;

import edu.epic.cms.exception.CardCreationException;
import edu.epic.cms.model.Card;
import edu.epic.cms.repository.CardRepo;
import edu.epic.cms.service.CardService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepo cardRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public CardServiceImpl(CardRepo cardRepo) {
        this.cardRepo = cardRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public List<Card> getAllCards() {
        return cardRepo.getAllCards();
    }

    @Override
    public boolean createCard(Card card) {
        if (card.getCardNumber() == null || card.getCardNumber().isEmpty()) {
            throw new CardCreationException("Card number is required");
        }

        String encryptedCardNumber = passwordEncoder.encode(card.getCardNumber());
        card.setCardNumber(encryptedCardNumber);

        return cardRepo.createCard(card);
    }
}
