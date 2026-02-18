package edu.epic.cms.service.impl;

import edu.epic.cms.model.Card;
import edu.epic.cms.repository.CardRepo;
import edu.epic.cms.service.CardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepo cardRepo;

    public CardServiceImpl(CardRepo cardRepo) {
        this.cardRepo = cardRepo;
    }

    @Override
    public List<Card> getAllCards() {
        return cardRepo.getAllCards();
    }
}
