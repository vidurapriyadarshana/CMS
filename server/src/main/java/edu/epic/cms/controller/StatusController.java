package edu.epic.cms.controller;

import edu.epic.cms.api.CommonResponse;
import edu.epic.cms.model.Status;
import edu.epic.cms.service.StatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/status")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAllStatus() {
        List<Status> statusList = statusService.getAllStatus();
        return ResponseEntity.ok(CommonResponse.success(statusList));
    }
}
