package com.martinPluhar.Bankapplication.services.impl;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.martinPluhar.Bankapplication.dto.EmailDetails;
import com.martinPluhar.Bankapplication.entity.Transaction;
import com.martinPluhar.Bankapplication.entity.User;
import com.martinPluhar.Bankapplication.repository.TransactionRepository;
import com.martinPluhar.Bankapplication.repository.UserRepository;
import com.martinPluhar.Bankapplication.services.intfc.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {
    /**
     * retrieve list of transactions within a date range given an account number
     * generate a pdf file of transactions
     * send file via email
     */

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;


    //Location where file will be generated
    private static final String FILE = System.getProperty("user.home") + "/Downloads/MyStatement.pdf";

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate)
            throws FileNotFoundException, DocumentException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List<Transaction> transactionList = transactionRepository.findByAccountNumberAndCreatedAtBetween(
                accountNumber, start, end);
        User user = userRepository.findByAccountNumber(accountNumber);

        Document document = createDocument();
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        addBankInfo(document);
        addStatementInfo(document, startDate, endDate, user);
        addTransactionTable(document, transactionList);

        document.close();

        sendStatementByEmail(user.getEmail());

        return transactionList;
    }

    private Document createDocument() throws DocumentException {
        Rectangle statementSize = new Rectangle(PageSize.A4);
        return new Document(statementSize);
    }

    private void addBankInfo(Document document) throws DocumentException {
        PdfPTable bankInfoTable = createTable(1);
        bankInfoTable.addCell(createCell("KB Bank", BaseColor.RED, 10f));
        bankInfoTable.addCell(createCell("Bankovní 22, Praha 6"));
        document.add(bankInfoTable);
    }

    private void addStatementInfo(Document document, String startDate, String endDate, User user) throws DocumentException {
        PdfPTable statementInfo = createTable(2);
        addCell(statementInfo, "Od data: " + startDate, 2);
        addCell(statementInfo, "Výpis transakcí", 2);
        addCell(statementInfo, "Do data: " + endDate, 2);
        addCell(statementInfo, "Jméno zákazníka: " + user.getFullName(), 2);
        addCell(statementInfo, "Adresa: " + user.getAddress(), 2);
        document.add(statementInfo);
    }

    private void addTransactionTable(Document document, List<Transaction> transactionList) throws DocumentException {
        PdfPTable transactionsTable = createTable(4);
        addCell(transactionsTable, "DATE", BaseColor.RED);
        addCell(transactionsTable, "TRANSACTION TYPE", BaseColor.RED);
        addCell(transactionsTable, "TRANSACTION AMOUNT", BaseColor.RED);
        addCell(transactionsTable, "STATUS", BaseColor.RED);

        for (Transaction transaction : transactionList) {
            addCell(transactionsTable, transaction.getCreatedAt().toString(), 2);
            addCell(transactionsTable, transaction.getTransactionType(), 2);
            addCell(transactionsTable, transaction.getAmount().toString(), 2);
            addCell(transactionsTable, transaction.getStatus(), 2);
        }
        document.add(transactionsTable);
    }

    private PdfPTable createTable(int numColumns) {
        return new PdfPTable(numColumns);
    }

    private PdfPCell createCell(String text, BaseColor backgroundColor, float padding) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBorder(0);
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(padding);
        return cell;
    }

    private PdfPCell createCell(String text) {
        return createCell(text, BaseColor.WHITE, 2f);
    }

    private void addCell(PdfPTable table, String text, int i) {
        table.addCell(createCell(text));
    }

    private void addCell(PdfPTable table, String text, BaseColor backgroundColor) {
        PdfPCell cell = createCell(text, backgroundColor, 2f);
        table.addCell(cell);
    }

    private void sendStatementByEmail(String recipientEmail) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(recipientEmail)
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Kindly find your requested account statement attached!")
                .attachment(FILE)
                .build();
        emailService.sendEmailWithAttachment(emailDetails);
    }

}
