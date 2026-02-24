package edu.epic.cms.repository;

import edu.epic.cms.model.CardRequest;

import java.util.List;

public interface CardRequestRepo {
    boolean createCardRequest(CardRequest cardRequest);
    boolean hasPendingRequest(String cardNumber);
    boolean updateStatusByCardNumber(String cardNumber, String status, String approvedUser, String requestStatus);
    boolean markRequestAsFailed(String cardNumber, String approvedUser);
    boolean markRequestAsDeactivated(String cardNumber);
    boolean isCardDeactivated(String cardNumber);
    List<CardRequest> getAllCardRequests();
    List<CardRequest> getCardRequestsByCardNumber(String cardNumber);

}
