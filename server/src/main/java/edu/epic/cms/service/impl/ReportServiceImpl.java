package edu.epic.cms.service.impl;

import edu.epic.cms.api.CardReportDTO;
import edu.epic.cms.api.CardRequestReportDTO;
import edu.epic.cms.repository.ReportRepo;
import edu.epic.cms.service.ReportService;
import edu.epic.cms.util.ReportGeneratorUtil;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepo reportRepo;

    public ReportServiceImpl(ReportRepo reportRepo) {
        this.reportRepo = reportRepo;
    }

    @Override
    public ByteArrayInputStream generateCardReportPdf(String cardStatus) {
        List<CardReportDTO> cards = reportRepo.getAllCardsWithUserNames(cardStatus);
        return ReportGeneratorUtil.cardReportToPdf(cards, cardStatus);
    }

    @Override
    public ByteArrayInputStream generateCardReportCsv(String cardStatus) {
        List<CardReportDTO> cards = reportRepo.getAllCardsWithUserNames(cardStatus);
        return ReportGeneratorUtil.cardReportToCsv(cards);
    }

    @Override
    public ByteArrayInputStream generateCardRequestReportPdf(String requestReasonCode, String requestStatus) {
        List<CardRequestReportDTO> requests = reportRepo.getAllCardRequestsWithUserNames(requestReasonCode, requestStatus);
        return ReportGeneratorUtil.cardRequestReportToPdf(requests, requestReasonCode, requestStatus);
    }

    @Override
    public ByteArrayInputStream generateCardRequestReportCsv(String requestReasonCode, String requestStatus) {
        List<CardRequestReportDTO> requests = reportRepo.getAllCardRequestsWithUserNames(requestReasonCode, requestStatus);
        return ReportGeneratorUtil.cardRequestReportToCsv(requests);
    }
}
