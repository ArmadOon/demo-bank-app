package com.martinPluhar.Bankapplication.services.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.martinPluhar.Bankapplication.dto.EmailDetails;
import com.martinPluhar.Bankapplication.entity.Transaction;
import com.martinPluhar.Bankapplication.entity.User;
import com.martinPluhar.Bankapplication.repository.TransactionRepository;
import com.martinPluhar.Bankapplication.repository.UserRepository;
import com.martinPluhar.Bankapplication.services.intfc.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private static final String FILE = System.getProperty("user.home") + "/Downloads/MyStatement.pdf";

    private static final String BANK_LOGO_PATH = "/Users/martinpluhy/Documents/KB-Bank-Demo/Bank-application/src/main/resources/static/kb_logo-(1).png"; // Nahraďte cestou k logu banky

    private Font titleFont;
    private Font subtitleFont;
    private Font headerFont;
    private Font contentFont;

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate)
            throws IOException, DocumentException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        List<Transaction> transactionList = transactionRepository.findBySenderAccountAndCreatedAtBetween(
                accountNumber, start, end);

        User user = userRepository.findByAccountNumber(accountNumber);

        Document document = createDocument();

        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);


        Image bankLogo = Image.getInstance(BANK_LOGO_PATH);
        bankLogo.setAlignment(Element.ALIGN_CENTER);

        document.open();
        document.add(bankLogo);

        addStatementTitle(document, startDate, endDate);
        addStatementInfo(document, user);

        PdfPTable transactionsTable = createTransactionTable(transactionList);
        document.add(transactionsTable);

        addPageNumberToFooter(writer);

        document.close();
        sendStatementByEmail(user.getEmail());

        return transactionList;
    }






    private Document createDocument() {
        Rectangle statementSize = new Rectangle(PageSize.A4);
        return new Document(statementSize);
    }

    private void addStatementTitle(Document document, String startDate, String endDate) throws DocumentException {
        Paragraph title = new Paragraph("Výpis transakcí", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);

        Paragraph subtitle = new Paragraph("Od: " + startDate + " Do: " + endDate, subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);

        document.add(title);
        document.add(subtitle);
    }

    private void addStatementInfo(Document document, User user) throws DocumentException {
        PdfPTable statementInfo = createTable(2);
        addCell(statementInfo, "Jméno zákazníka: " + user.getFullName(), headerFont, BaseColor.WHITE);
        addCell(statementInfo, "Adresa: " + user.getAddress(), headerFont, BaseColor.WHITE);

        document.add(statementInfo);
    }

    private PdfPTable createTransactionTable(List<Transaction> transactionList) throws DocumentException {
        PdfPTable transactionsTable = createTable(6);
        transactionsTable.setWidthPercentage(100);
        transactionsTable.setWidths(new float[]{1, 2, 2, 2, 2, 2});
        transactionsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell headerCell = createCell("Datum", headerFont, BaseColor.GRAY);
        transactionsTable.addCell(headerCell);

        PdfPCell senderCell = createCell("Odesílatel", headerFont, BaseColor.GRAY);
        transactionsTable.addCell(senderCell);

        PdfPCell receiverCell = createCell("Příjemce", headerFont, BaseColor.GRAY);
        transactionsTable.addCell(receiverCell);

        PdfPCell statusCell = createCell("Typ", headerFont, BaseColor.GRAY);
        transactionsTable.addCell(statusCell);
        PdfPCell amountCell = createCell("Transakce", headerFont, BaseColor.GRAY);
        transactionsTable.addCell(amountCell);
        PdfPCell typeCell = createCell("Status", headerFont, BaseColor.GRAY);
        transactionsTable.addCell(typeCell);

        for (Transaction transaction : transactionList) {
            addCell(transactionsTable, transaction.getCreatedAt().toString(), contentFont, BaseColor.WHITE);
            addCell(transactionsTable, transaction.getSenderAccount(), contentFont, BaseColor.WHITE);
            addCell(transactionsTable, transaction.getReceiverAccount(), contentFont, BaseColor.WHITE);
            addCell(transactionsTable, transaction.getTransactionType(), contentFont, BaseColor.WHITE);
            addCell(transactionsTable, transaction.getAmount().toString(), contentFont, BaseColor.WHITE);
            addCell(transactionsTable, transaction.getStatus(), contentFont, BaseColor.WHITE);
        }

        return transactionsTable;
    }

    private void addPageNumberToFooter(PdfWriter writer) {
        Phrase pageNumber = new Phrase("Strana " + writer.getPageNumber(), headerFont);
        PdfPageEvent event = new PdfPageEvent() {
            @Override
            public void onOpenDocument(PdfWriter writer, Document document) {

            }

            @Override
            public void onStartPage(PdfWriter writer, Document document) {

            }

            @Override
            public void onEndPage(PdfWriter writer, Document document) {
                PdfContentByte canvas = writer.getDirectContent();
                ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, pageNumber,
                        (document.right() - document.left()) / 2 + document.leftMargin(),
                        document.bottom() - 10, 0);
            }

            @Override
            public void onCloseDocument(PdfWriter writer, Document document) {

            }

            @Override
            public void onParagraph(PdfWriter writer, Document document, float paragraphPosition) {

            }

            @Override
            public void onParagraphEnd(PdfWriter writer, Document document, float paragraphPosition) {

            }

            @Override
            public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {

            }

            @Override
            public void onChapterEnd(PdfWriter writer, Document document, float paragraphPosition) {

            }

            @Override
            public void onSection(PdfWriter writer, Document document, float paragraphPosition, int depth, Paragraph title) {

            }

            @Override
            public void onSectionEnd(PdfWriter writer, Document document, float paragraphPosition) {

            }

            @Override
            public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {

            }
        };

        writer.setPageEvent(event);
    }

    private PdfPTable createTable(int numColumns) {
        return new PdfPTable(numColumns);
    }

    private PdfPCell createCell(String text, Font font, BaseColor backgroundColor, float padding) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(0);
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(padding);
        return cell;
    }

    private PdfPCell createCell(String text, Font font, BaseColor backgroundColor) {
        return createCell(text, font, backgroundColor, 2f);
    }

    private void addCell(PdfPTable table, String text, Font font, BaseColor backgroundColor) {
        table.addCell(createCell(text, font, backgroundColor));
    }

    private void sendStatementByEmail(String recipientEmail)  {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(recipientEmail)
                .subject("VÝPIS ÚČTU")
                .messageBody("Váš požadovaný výpis účtu je v příloze!")
                .attachment(FILE)
                .build();
        emailService.sendEmailWithAttachment(emailDetails);
    }
}

