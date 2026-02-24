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
    public ByteArrayInputStream generateCardReportPdf() {
        List<CardReportDTO> cards = reportRepo.getAllCardsWithUserNames();
        return ReportGeneratorUtil.cardReportToPdf(cards);
    }

    @Override
    public ByteArrayInputStream generateCardReportCsv() {
        List<CardReportDTO> cards = reportRepo.getAllCardsWithUserNames();
        return ReportGeneratorUtil.cardReportToCsv(cards);
    }

    @Override
    public ByteArrayInputStream generateCardRequestReportPdf() {
        List<CardRequestReportDTO> requests = reportRepo.getAllCardRequestsWithUserNames();
        return ReportGeneratorUtil.cardRequestReportToPdf(requests);
    }

    @Override
    public ByteArrayInputStream generateCardRequestReportCsv() {
        List<CardRequestReportDTO> requests = reportRepo.getAllCardRequestsWithUserNames();
        return ReportGeneratorUtil.cardRequestReportToCsv(requests);
    }
}
