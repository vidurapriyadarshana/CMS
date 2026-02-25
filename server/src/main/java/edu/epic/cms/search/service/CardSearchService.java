package edu.epic.cms.search.service;

import edu.epic.cms.dto.CardResponseDTO;
import java.util.List;

public interface CardSearchService {
    List<CardResponseDTO> searchCards(String cardNumber, String cardStatus);
}
