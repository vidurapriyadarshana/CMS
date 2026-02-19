package edu.epic.cms.repository;

import edu.epic.cms.model.CardRequest;

public interface CardRequestRepo {
    boolean createCardRequest(CardRequest cardRequest);
    boolean hasPendingRequest(String cardNumber);
}

