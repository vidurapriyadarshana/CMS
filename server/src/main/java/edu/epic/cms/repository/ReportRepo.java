package edu.epic.cms.repository;

import edu.epic.cms.api.CardReportDTO;
import edu.epic.cms.api.CardRequestReportDTO;

import java.util.List;

public interface ReportRepo {
    List<CardReportDTO> getAllCardsWithUserNames(String cardStatus);
    List<CardRequestReportDTO> getAllCardRequestsWithUserNames(String requestReasonCode, String requestStatus);
}
