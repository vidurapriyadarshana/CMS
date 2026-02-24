package edu.epic.cms.service;

import java.io.ByteArrayInputStream;

public interface ReportService {
    ByteArrayInputStream generateCardReportPdf();
    ByteArrayInputStream generateCardReportCsv();
    ByteArrayInputStream generateCardRequestReportPdf();
    ByteArrayInputStream generateCardRequestReportCsv();
}
