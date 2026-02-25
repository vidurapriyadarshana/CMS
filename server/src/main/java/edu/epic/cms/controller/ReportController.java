package edu.epic.cms.controller;

import edu.epic.cms.service.ReportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/cards/pdf")
    public ResponseEntity<InputStreamResource> downloadCardPdf(
            @RequestParam(required = false) String cardStatus) {
        ByteArrayInputStream bis = reportService.generateCardReportPdf(cardStatus);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=card_report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/cards/csv")
    public ResponseEntity<InputStreamResource> downloadCardCsv(
            @RequestParam(required = false) String cardStatus) {
        ByteArrayInputStream bis = reportService.generateCardReportCsv(cardStatus);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=card_report.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/card-requests/pdf")
    public ResponseEntity<InputStreamResource> downloadCardRequestPdf(
            @RequestParam(required = false) String requestReasonCode,
            @RequestParam(required = false) String requestStatus) {
        ByteArrayInputStream bis = reportService.generateCardRequestReportPdf(requestReasonCode, requestStatus);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=card_request_report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/card-requests/csv")
    public ResponseEntity<InputStreamResource> downloadCardRequestCsv(
            @RequestParam(required = false) String requestReasonCode,
            @RequestParam(required = false) String requestStatus) {
        ByteArrayInputStream bis = reportService.generateCardRequestReportCsv(requestReasonCode, requestStatus);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=card_request_report.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(bis));
    }
}
