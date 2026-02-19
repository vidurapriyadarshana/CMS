package edu.epic.cms.service.impl;

import edu.epic.cms.model.CardRequestType;
import edu.epic.cms.repository.CardRequestTypeRepo;
import edu.epic.cms.service.CardRequestTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardRequestTypeServiceImpl implements CardRequestTypeService {

    private final CardRequestTypeRepo cardRequestTypeRepo;

    public CardRequestTypeServiceImpl(CardRequestTypeRepo cardRequestTypeRepo) {
        this.cardRequestTypeRepo = cardRequestTypeRepo;
    }

    @Override
    public List<CardRequestType> getAllCardRequestTypes() {
        return cardRequestTypeRepo.getAllCardRequestTypes();
    }
}
