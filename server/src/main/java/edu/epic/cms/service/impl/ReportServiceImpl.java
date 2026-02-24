package edu.epic.cms.service.impl;

import edu.epic.cms.api.CardReportDTO;
import edu.epic.cms.api.CardRequestReportDTO;
import edu.epic.cms.repository.CardRepo;
import edu.epic.cms.repository.CardRequestRepo;
import edu.epic.cms.service.ReportService;
import edu.epic.cms.util.ReportGeneratorUtil;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final CardRepo cardRepo;
    private final CardRequestRepo cardRequestRepo;

    public ReportServiceImpl(CardRepo cardRepo, CardRequestRepo cardRequestRepo) {
        this.cardRepo = cardRepo;
        this.cardRequestRepo = cardRequestRepo;
    }

    @Override
    public ByteArrayInputStream generateCardReportPdf() {
        List<CardReportDTO> cards = cardRepo.getAllCardsWithUserNames();
        return ReportGeneratorUtil.cardReportToPdf(cards);
    }

    @Override
    public ByteArrayInputStream generateCardReportCsv() {
        List<CardReportDTO> cards = cardRepo.getAllCardsWithUserNames();
        return ReportGeneratorUtil.cardReportToCsv(cards);
    }

    @Override
    public ByteArrayInputStream generateCardRequestReportPdf() {
        List<CardRequestReportDTO> requests = cardRequestRepo.getAllCardRequestsWithUserNames();
        return ReportGeneratorUtil.cardRequestReportToPdf(requests);
    }

    @Override
    public ByteArrayInputStream generateCardRequestReportCsv() {
        List<CardRequestReportDTO> requests = cardRequestRepo.getAllCardRequestsWithUserNames();
        return ReportGeneratorUtil.cardRequestReportToCsv(requests);
    }
}
