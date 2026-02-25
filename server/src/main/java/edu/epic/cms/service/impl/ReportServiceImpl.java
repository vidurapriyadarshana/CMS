package edu.epic.cms.service.impl;

import edu.epic.cms.dto.CardReportDTO;
import edu.epic.cms.dto.CardRequestReportDTO;
import edu.epic.cms.exception.ReportDataNotFoundException;
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
        if (cards == null || cards.isEmpty()) {
            throw new ReportDataNotFoundException("No card data found for the given criteria.");
        }
        return ReportGeneratorUtil.cardReportToPdf(cards, cardStatus);
    }

    @Override
    public ByteArrayInputStream generateCardReportCsv(String cardStatus) {
        List<CardReportDTO> cards = reportRepo.getAllCardsWithUserNames(cardStatus);
        if (cards == null || cards.isEmpty()) {
            throw new ReportDataNotFoundException("No card data found for the given criteria.");
        }
        return ReportGeneratorUtil.cardReportToCsv(cards);
    }

    @Override
    public ByteArrayInputStream generateCardRequestReportPdf(String requestReasonCode, String requestStatus) {
        List<CardRequestReportDTO> requests = reportRepo.getAllCardRequestsWithUserNames(requestReasonCode, requestStatus);
        if (requests == null || requests.isEmpty()) {
            throw new ReportDataNotFoundException("No card request data found for the given criteria.");
        }
        return ReportGeneratorUtil.cardRequestReportToPdf(requests, requestReasonCode, requestStatus);
    }

    @Override
    public ByteArrayInputStream generateCardRequestReportCsv(String requestReasonCode, String requestStatus) {
        List<CardRequestReportDTO> requests = reportRepo.getAllCardRequestsWithUserNames(requestReasonCode, requestStatus);
        if (requests == null || requests.isEmpty()) {
            throw new ReportDataNotFoundException("No card request data found for the given criteria.");
        }
        return ReportGeneratorUtil.cardRequestReportToCsv(requests);
    }
}
