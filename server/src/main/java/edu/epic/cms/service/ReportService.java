package edu.epic.cms.service;

import java.io.ByteArrayInputStream;

public interface ReportService {
    ByteArrayInputStream generateCardReportPdf(String cardStatus);
    ByteArrayInputStream generateCardReportCsv(String cardStatus);
    ByteArrayInputStream generateCardRequestReportPdf(String requestReasonCode, String requestStatus);
    ByteArrayInputStream generateCardRequestReportCsv(String requestReasonCode, String requestStatus);
}
