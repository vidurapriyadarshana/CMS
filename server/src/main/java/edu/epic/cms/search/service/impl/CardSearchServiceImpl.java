package edu.epic.cms.search.service.impl;

import edu.epic.cms.api.CardResponseDTO;
import edu.epic.cms.model.Card;
import edu.epic.cms.search.repository.CardSearchRepository;
import edu.epic.cms.search.service.CardSearchService;
import edu.epic.cms.util.CardEncryptionUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardSearchServiceImpl implements CardSearchService {

    private final CardSearchRepository cardSearchRepository;

    public CardSearchServiceImpl(CardSearchRepository cardSearchRepository) {
        this.cardSearchRepository = cardSearchRepository;
    }

    @Override
    public List<CardResponseDTO> searchCards(String cardNumber, String cardStatus) {
        String encryptedCardNumber = null;
        if (cardNumber != null && !cardNumber.isEmpty()) {
            encryptedCardNumber = CardEncryptionUtil.encrypt(cardNumber);
        }

        List<Card> cards = cardSearchRepository.searchCards(encryptedCardNumber, cardStatus);

        return cards.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private CardResponseDTO mapToDTO(Card card) {
        CardResponseDTO dto = new CardResponseDTO();
        dto.setEncryptedCardNumber(card.getCardNumber());
        dto.setCardNumber(CardEncryptionUtil.maskCardNumber(CardEncryptionUtil.decrypt(card.getCardNumber())));
        dto.setExpireDate(card.getExpireDate());
        dto.setCardStatus(card.getCardStatusDescription() != null ? card.getCardStatusDescription() : card.getCardStatus());
        dto.setCreditLimit(card.getCreditLimit());
        dto.setCashLimit(card.getCashLimit());
        dto.setAvailableCreditLimit(card.getAvailableCreditLimit());
        dto.setAvailableCashLimit(card.getAvailableCashLimit());
        dto.setLastUpdateTime(card.getLastUpdateTime());
        dto.setLastUpdatedUser(card.getLastUpdatedUser());
        return dto;
    }
}
