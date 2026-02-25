package edu.epic.cms.service;

import edu.epic.cms.dto.CardResponseDTO;
import edu.epic.cms.dto.UpdateCardDTO;
import edu.epic.cms.dto.CreateCardRequestDTO;


import java.util.List;

public interface CardService {
    List<CardResponseDTO> getAllCards(String cardStatus);
    boolean createCard(CreateCardRequestDTO request);
    boolean updateCard(String encryptedCardNumber, UpdateCardDTO updateCard);
    CardResponseDTO getCardByEncryptedNumber(String encryptedCardNumber);
    boolean deleteCard(String encryptedCardNumber);
}

