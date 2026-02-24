package edu.epic.cms.service;

import edu.epic.cms.api.CardResponseDTO;
import edu.epic.cms.api.UpdateCardDTO;
import edu.epic.cms.api.CreateCardRequestDTO;


import java.util.List;

public interface CardService {
    List<CardResponseDTO> getAllCards();
    boolean createCard(CreateCardRequestDTO request);
    boolean updateCard(String encryptedCardNumber, UpdateCardDTO updateCard);
    CardResponseDTO getCardByEncryptedNumber(String encryptedCardNumber);
    boolean deleteCard(String encryptedCardNumber);
}

