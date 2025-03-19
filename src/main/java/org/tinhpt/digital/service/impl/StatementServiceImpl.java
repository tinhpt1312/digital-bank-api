package org.tinhpt.digital.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.tinhpt.digital.entity.Account;
import org.tinhpt.digital.entity.Transaction;
import org.tinhpt.digital.service.EmailService;
import org.tinhpt.digital.service.StatementService;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatementServiceImpl implements StatementService {

    private final EmailService emailService;

    @Override
    public Resource generateAccountStatementPdf(Account account, List<Transaction> transactions) throws IOException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("ACCOUNT STATEMENT", titleFont);

            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            addAccountInfo(document, account);
            document.add(new Paragraph(" "));

            addTransactionsTable(document, transactions, account);
            document.close();

            return new ByteArrayResource(outputStream.toByteArray());
        } catch (DocumentException e) {
            throw new IOException("Error generating PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendAccountStatementByEmail(Account account, List<Transaction> transactions, String email)
            throws IOException {
        ByteArrayResource pdfResource = (ByteArrayResource) generateAccountStatementPdf(account, transactions);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String fileName = "account_statement_" + account.getAccountNumber() + "_" + dateFormat.format(new Date())
                + ".pdf";

        String subject = "Account Statement - " + account.getAccountNumber();
        String body = "Dear Customer,\n\n"
                + "Please find attached your account statement for account number " + account.getAccountNumber()
                + ".\n\n"
                + "Thank you for banking with us.\n\n"
                + "Best regards,\n"
                + "Digital Bank Team";

        emailService.sendEmailWithAttachment(email, subject, body, fileName, pdfResource.getByteArray(),
                "application/pdf");
    }

    private void addAccountInfo(Document document, Account account) throws DocumentException {
        Font boldFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.TIMES, 12);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        PdfPCell cell1 = new PdfPCell(new Phrase("Account Number:", boldFont));
        cell1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase(account.getAccountNumber(), normalFont));
        cell2.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell2);

        PdfPCell cell3 = new PdfPCell(new Phrase("Account Type:", boldFont));
        cell3.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell3);

        PdfPCell cell4 = new PdfPCell(new Phrase(account.getAccountType(), normalFont));
        cell4.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell4);

        PdfPCell cell5 = new PdfPCell(new Phrase("Currency:", boldFont));
        cell5.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell5);

        PdfPCell cell6 = new PdfPCell(new Phrase(account.getCurrency(), normalFont));
        cell6.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell6);

        PdfPCell cell7 = new PdfPCell(new Phrase("Current Balance:", boldFont));
        cell7.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell7);

        String formattedBalance = formatCurrency(account.getBalance(), account.getCurrency());
        PdfPCell cell8 = new PdfPCell(new Phrase(formattedBalance, normalFont));
        cell8.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell8);

        document.add(table);
    }

    private void addTransactionsTable(Document document, List<Transaction> transactions, Account account)
            throws DocumentException {
        Font headerFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 12, BaseColor.WHITE);
        Font cellFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        float[] columnWidths = { 0.7f, 2f, 1.5f, 2.8f, 1.5f, 1.5f };
        table.setWidths(columnWidths);

        String[] headers = { "No.", "Date", "Type", "Description", "Amount", "Status" };
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, headerFont));

            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String currency = account.getCurrency();

        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);

            PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(i + 1), cellFont));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(
                    new Paragraph(dateFormat.format(transaction.getAudit().getCreatedAt()), cellFont));
            table.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Phrase(transaction.getTransactionType(), cellFont));
            table.addCell(cell3);

            PdfPCell cell4 = new PdfPCell(new Phrase(transaction.getDescription(), cellFont));
            table.addCell(cell4);

            String formattedAmount = formatCurrency(transaction.getAmount(), currency);
            PdfPCell cell5 = new PdfPCell(new Phrase(formattedAmount, cellFont));
            cell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell5);

            PdfPCell cell6 = new PdfPCell(new Phrase(transaction.getStatus(), cellFont));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell6);
        }

        document.add(table);
    }

    /**
     * Định dạng số tiền theo loại tiền tệ
     * 
     * @param amount   Số tiền cần định dạng
     * @param currency Loại tiền tệ (VND, USD, ...)
     * @return Chuỗi đã được định dạng
     */
    private String formatCurrency(BigDecimal amount, String currency) {
        if (amount == null) {
            return "0";
        }

        @SuppressWarnings("deprecation")
        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new Locale("vi", "VN"));
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(0);

        String formattedAmount = formatter.format(amount);

        if ("VND".equalsIgnoreCase(currency)) {
            return formattedAmount + " ₫";
        } else if ("USD".equalsIgnoreCase(currency)) {
            return "$" + formattedAmount;
        } else {
            return formattedAmount + " " + currency;
        }
    }

}
