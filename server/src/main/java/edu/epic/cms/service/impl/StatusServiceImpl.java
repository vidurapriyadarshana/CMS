package edu.epic.cms.service.impl;

import edu.epic.cms.model.Status;
import edu.epic.cms.repository.StatusRepo;
import edu.epic.cms.service.StatusService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepo statusRepo;

    public StatusServiceImpl(StatusRepo statusRepo) {
        this.statusRepo = statusRepo;
    }

    @Override
    public List<Status> getAllStatus() {
        return statusRepo.getAllStatus();
    }
}
