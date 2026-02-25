package edu.epic.cms.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import edu.epic.cms.dto.CardReportDTO;
import edu.epic.cms.dto.CardRequestReportDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportGeneratorUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ByteArrayInputStream cardReportToPdf(List<CardReportDTO> cards, String cardStatus) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(18);
            font.setColor(Color.BLUE);

            Paragraph p = new Paragraph("Card Report", font);
            p.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p);

            Font filterFont = FontFactory.getFont(FontFactory.HELVETICA);
            filterFont.setSize(10);
            
            StringBuilder filters = new StringBuilder();
            filters.append("Generated Date: ").append(java.time.LocalDateTime.now().format(DATE_TIME_FORMATTER));
            if (cardStatus != null && !cardStatus.isEmpty()) {
                filters.append(" | Filter [Status: ").append(cardStatus).append("]");
            }
            
            Paragraph filterPara = new Paragraph(filters.toString(), filterFont);
            filterPara.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(filterPara);

            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100f);
            table.setWidths(new float[]{3.5f, 2.0f, 2.5f, 2.0f, 2.0f, 2.0f, 2.0f, 3.0f, 3.0f});
            table.setSpacingBefore(10);

            writeCardTableHeader(table);
            writeCardTableData(table, cards);

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private static void writeCardTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        String[] headers = {"Card Number", "Expire", "Status", "Credit", "Cash", "Avail Cr", "Avail Ca", "Updated Time", "Updated User"};
        for (String header : headers) {
            cell.setPhrase(new Phrase(header, font));
            table.addCell(cell);
        }
    }

    private static void writeCardTableData(PdfPTable table, List<CardReportDTO> cards) {
        for (CardReportDTO card : cards) {
            table.addCell(CardEncryptionUtil.maskCardNumber(CardEncryptionUtil.decrypt(card.getCardNumber())));
            table.addCell(card.getExpireDate());
            table.addCell(card.getCardStatusDescription() != null ? card.getCardStatusDescription() : card.getCardStatus());
            table.addCell(String.valueOf(card.getCreditLimit()));
            table.addCell(String.valueOf(card.getCashLimit()));
            table.addCell(String.valueOf(card.getAvailableCreditLimit()));
            table.addCell(String.valueOf(card.getAvailableCashLimit()));
            table.addCell(card.getLastUpdateTime() != null ? card.getLastUpdateTime().format(DATE_TIME_FORMATTER) : "");
            table.addCell(card.getLastUpdatedUserName() != null ? card.getLastUpdatedUserName() : card.getLastUpdatedUser());
        }
    }

    public static ByteArrayInputStream cardRequestReportToPdf(List<CardRequestReportDTO> requests, String reasonCode, String status) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(18);
            font.setColor(Color.BLUE);

            Paragraph p = new Paragraph("Card Request Report", font);
            p.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p);

            Font filterFont = FontFactory.getFont(FontFactory.HELVETICA);
            filterFont.setSize(10);

            StringBuilder filters = new StringBuilder();
            filters.append("Generated Date: ").append(java.time.LocalDateTime.now().format(DATE_TIME_FORMATTER));
            if (reasonCode != null && !reasonCode.isEmpty()) {
                filters.append(" | Filter [Reason: ").append(reasonCode).append("]");
            }
            if (status != null && !status.isEmpty()) {
                filters.append(" | Filter [Status: ").append(status).append("]");
            }

            Paragraph filterPara = new Paragraph(filters.toString(), filterFont);
            filterPara.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(filterPara);

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100f);
            table.setWidths(new float[]{1.0f, 2.5f, 3.0f, 3.5f, 3.0f, 2.5f, 2.5f, 2.0f});
            table.setSpacingBefore(10);

            writeCardRequestTableHeader(table);
            writeCardRequestTableData(table, requests);

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private static void writeCardRequestTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        String[] headers = {"ID", "Reason", "Remark", "Card Number", "Time", "Req Name", "App Name", "Status"};
        for (String header : headers) {
            cell.setPhrase(new Phrase(header, font));
            table.addCell(cell);
        }
    }

    private static void writeCardRequestTableData(PdfPTable table, List<CardRequestReportDTO> requests) {
        for (CardRequestReportDTO req : requests) {
            table.addCell(String.valueOf(req.getRequestId()));
            table.addCell(req.getRequestReasonDescription() != null ? req.getRequestReasonDescription() : req.getRequestReasonCode());
            table.addCell(req.getRemark());
            table.addCell(CardEncryptionUtil.maskCardNumber(CardEncryptionUtil.decrypt(req.getCardNumber())));
            table.addCell(req.getCreatedTime() != null ? req.getCreatedTime().format(DATE_TIME_FORMATTER) : "");
            table.addCell(req.getRequestedUserName() != null ? req.getRequestedUserName() : req.getRequestedUser());
            table.addCell(req.getApprovedUserName() != null ? req.getApprovedUserName() : req.getApprovedUser());
            table.addCell(req.getRequestStatus());
        }
    }

    public static ByteArrayInputStream cardReportToCsv(List<CardReportDTO> cards) {
        final CSVFormat format = CSVFormat.DEFAULT.withHeader("Card Number", "Expire Date", "Status", "Credit Limit", "Cash Limit", "Avail Credit", "Avail Cash", "Updated Time", "Updated User");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            for (CardReportDTO card : cards) {
                csvPrinter.printRecord(
                        CardEncryptionUtil.maskCardNumber(CardEncryptionUtil.decrypt(card.getCardNumber())),
                        card.getExpireDate(),
                        card.getCardStatusDescription() != null ? card.getCardStatusDescription() : card.getCardStatus(),
                        card.getCreditLimit(),
                        card.getCashLimit(),
                        card.getAvailableCreditLimit(),
                        card.getAvailableCashLimit(),
                        card.getLastUpdateTime() != null ? card.getLastUpdateTime().format(DATE_TIME_FORMATTER) : "",
                        card.getLastUpdatedUserName() != null ? card.getLastUpdatedUserName() : card.getLastUpdatedUser()
                );
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Fail to import data to CSV file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream cardRequestReportToCsv(List<CardRequestReportDTO> requests) {
        final CSVFormat format = CSVFormat.DEFAULT.withHeader("Request ID", "Reason", "Remark", "Card Number", "Created Time", "Requested User", "Approved User", "Status");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            for (CardRequestReportDTO req : requests) {
                csvPrinter.printRecord(
                        req.getRequestId(),
                        req.getRequestReasonDescription() != null ? req.getRequestReasonDescription() : req.getRequestReasonCode(),
                        req.getRemark(),
                        CardEncryptionUtil.maskCardNumber(CardEncryptionUtil.decrypt(req.getCardNumber())),
                        req.getCreatedTime() != null ? req.getCreatedTime().format(DATE_TIME_FORMATTER) : "",
                        req.getRequestedUserName() != null ? req.getRequestedUserName() : req.getRequestedUser(),
                        req.getApprovedUserName() != null ? req.getApprovedUserName() : req.getApprovedUser(),
                        req.getRequestStatus()
                );
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Fail to import data to CSV file: " + e.getMessage());
        }
    }
}
