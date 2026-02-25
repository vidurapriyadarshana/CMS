package edu.epic.cms.repository;

import edu.epic.cms.dto.CardReportDTO;
import edu.epic.cms.dto.CardRequestReportDTO;

import java.util.List;

public interface ReportRepo {
    List<CardReportDTO> getAllCardsWithUserNames(String cardStatus);
    List<CardRequestReportDTO> getAllCardRequestsWithUserNames(String requestReasonCode, String requestStatus);
}
