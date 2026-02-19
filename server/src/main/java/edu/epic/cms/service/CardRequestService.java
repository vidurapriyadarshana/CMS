package edu.epic.cms.service;

import edu.epic.cms.model.CardRequest;

public interface CardRequestService {
    boolean createCardRequest(CardRequest cardRequest);
    boolean updateStatus(String encryptedCardNumber, String status);
}

