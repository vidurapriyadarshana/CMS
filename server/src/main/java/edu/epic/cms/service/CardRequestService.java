package edu.epic.cms.service;

import edu.epic.cms.model.CardRequest;

import java.util.List;

public interface CardRequestService {
    boolean createCardRequest(CardRequest cardRequest);
    boolean updateStatus(String encryptedCardNumber, String status, String approvedUser);
    List<CardRequest> getAllCardRequests();
    List<CardRequest> getCardRequestsByCardNumber(String encryptedCardNumber);
}
