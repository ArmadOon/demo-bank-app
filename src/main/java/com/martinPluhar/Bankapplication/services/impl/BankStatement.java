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
    // root dir where pdf will be saved
    private static final String FILE = System.getProperty("user.home") + "/Downloads/MyStatement.pdf";
    // header logo dir
    private static final String BANK_LOGO_PATH = "/Users/martinpluhy/Documents/KB-Bank-Demo/Bank-application/src/main/resources/static/kb_logo-(1).png";

    private Font titleFont;
    private Font subtitleFont;
    private Font headerFont;
    private Font contentFont;

    /**
     * Metoda pro generování výpisu transakcí pro zadaný účet v určeném datovém rozmezí.
     *
     * @param accountNumber Číslo účtu, pro který má být výpis vygenerován.
     * @param startDate     Datum začátku rozmezí ve formátu "yyyy-MM-dd".
     * @param endDate       Datum konce rozmezí ve formátu "yyyy-MM-dd".
     * @return Seznam transakcí v daném rozmezí.
     * @throws IOException      Pokud dojde k chybě při práci se soubory.
     * @throws DocumentException Pokud dojde k chybě při práci s PDF dokumentem.
     */
    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate)
            throws IOException, DocumentException {
        // Převedení textových dat na LocalDate objekty pro zpracování dat.
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        // Získání seznamu transakcí v daném rozmezí pro zadaný účet.
        List<Transaction> transactionList = transactionRepository.findBySenderAccountAndCreatedAtBetween(
                accountNumber, start, end);

        // Získání informací o uživateli na základě čísla účtu.
        User user = userRepository.findByAccountNumber(accountNumber);

        // Vytvoření nového PDF dokumentu.
        Document document = createDocument();

        // Vytvoření výstupního proudu pro PDF soubor.
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        // Vytvoření instance obrázku bankovního loga a zarovnání ho na střed stránky.
        Image bankLogo = Image.getInstance(BANK_LOGO_PATH);
        bankLogo.setAlignment(Element.ALIGN_CENTER);

        // Otevření dokumentu a přidání bankovního loga.
        document.open();
        document.add(bankLogo);

        // Přidání titulu výpisu transakcí a období platnosti výpisu.
        addStatementTitle(document, startDate, endDate);

        // Přidání informací o uživateli (jméno, adresa) do dokumentu.
        addStatementInfo(document, user);

        // Vytvoření tabulky transakcí a přidání ji do dokumentu.
        PdfPTable transactionsTable = createTransactionTable(transactionList);
        document.add(transactionsTable);

        // Přidání čísla stránky do zápatí dokumentu.
        addPageNumberToFooter(writer);

        // Uzavření dokumentu a odeslání výpisu účtu e-mailem na e-mail uživatele.
        document.close();
        sendStatementByEmail(user.getEmail());

        // Vrácení seznamu transakcí.
        return transactionList;
    }





    // Create document with A4 size
    private Document createDocument() {
        Rectangle statementSize = new Rectangle(PageSize.A4);
        return new Document(statementSize);
    }
    //
    /**
     * Metoda pro přidání titulní části do PDF dokumentu.
     *
     * @param document  PDF dokument, do kterého se přidá titulní část.
     * @param startDate Začátek časového rozsahu transakcí.
     * @param endDate   Konec časového rozsahu transakcí.
     * @throws DocumentException Pokud dojde k chybě při práci s dokumentem.
     */
    private void addStatementTitle(Document document, String startDate, String endDate) throws DocumentException {
        // Vytvoření titulku "Výpis transakcí"
        Paragraph title = new Paragraph("Výpis transakcí", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);

        // Vytvoření podtitulku s informacemi o časovém rozsahu
        Paragraph subtitle = new Paragraph("Od: " + startDate + " Do: " + endDate, subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);

        // Přidání titulku a podtitulku do dokumentu
        document.add(title);
        document.add(subtitle);
    }

    /**
     * Metoda pro přidání informací o uživateli do PDF dokumentu.
     *
     * @param document PDF dokument, do kterého se přidá informace o uživateli.
     * @param user     Uživatel, jehož informace se přidají do dokumentu.
     * @throws DocumentException Pokud dojde k chybě při práci s dokumentem.
     */
    private void addStatementInfo(Document document, User user) throws DocumentException {
        // Vytvoření tabulky pro zobrazení informací o uživateli
        PdfPTable statementInfo = createTable(2);

        // Přidání buňky s jménem uživatele do tabulky
        addCell(statementInfo, "Jméno zákazníka: " + user.getFullName(), headerFont, BaseColor.WHITE);

        // Přidání buňky s adresou uživatele do tabulky
        addCell(statementInfo, "Adresa: " + user.getAddress(), headerFont, BaseColor.WHITE);

        // Přidání tabulky do dokumentu
        document.add(statementInfo);
    }

    /**
     * Metoda pro vytvoření tabulky transakcí v PDF dokumentu.
     *
     * @param transactionList Seznam transakcí, které budou zobrazeny v tabulce.
     * @return Vytvořená tabulka transakcí.
     * @throws DocumentException Pokud dojde k chybě při práci s dokumentem.
     */
    private PdfPTable createTransactionTable(List<Transaction> transactionList) throws DocumentException {
        // Vytvoření prázdné tabulky pro transakce s 6 sloupci
        PdfPTable transactionsTable = createTable(6);

        // Nastavení šířky tabulky na 100 % šířky stránky
        transactionsTable.setWidthPercentage(100);

        // Nastavení šířek sloupců v tabulce
        transactionsTable.setWidths(new float[]{1, 2, 2, 2, 2, 2});

        // Zarovnání obsahu tabulky na střed
        transactionsTable.setHorizontalAlignment(Element.ALIGN_CENTER);

        // Vytvoření buňky pro záhlaví s textem "Datum"
        PdfPCell headerCell = createCell("Datum", headerFont, BaseColor.GRAY);
        transactionsTable.addCell(headerCell);

        // Vytvoření buňky pro záhlaví s textem "Odesílatel"
        PdfPCell senderCell = createCell("Odesílatel", headerFont, BaseColor.GRAY);
        transactionsTable.addCell(senderCell);

        // Vytvoření buňky pro záhlaví s textem "Příjemce"
        PdfPCell receiverCell = createCell("Příjemce", headerFont, BaseColor.GRAY);
        transactionsTable.addCell(receiverCell);

        // Vytvoření buňky pro záhlaví s textem "Typ"
        PdfPCell statusCell = createCell("Typ", headerFont, BaseColor.GRAY);
        transactionsTable.addCell(statusCell);

        // Vytvoření buňky pro záhlaví s textem "Transakce"
        PdfPCell amountCell = createCell("Transakce", headerFont, BaseColor.GRAY);
        transactionsTable.addCell(amountCell);

        // Vytvoření buňky pro záhlaví s textem "Status"
        PdfPCell typeCell = createCell("Status", headerFont, BaseColor.GRAY);
        transactionsTable.addCell(typeCell);

        // Pro každou transakci v seznamu transakcí přidej odpovídající řádek do tabulky
        for (Transaction transaction : transactionList) {
            addCell(transactionsTable, transaction.getCreatedAt().toString(), contentFont, BaseColor.WHITE);
            addCell(transactionsTable, transaction.getSenderAccount(), contentFont, BaseColor.WHITE);
            addCell(transactionsTable, transaction.getReceiverAccount(), contentFont, BaseColor.WHITE);
            addCell(transactionsTable, transaction.getTransactionType(), contentFont, BaseColor.WHITE);
            addCell(transactionsTable, transaction.getAmount().toString(), contentFont, BaseColor.WHITE);
            addCell(transactionsTable, transaction.getStatus(), contentFont, BaseColor.WHITE);
        }

        // Vrať vytvořenou tabulku transakcí
        return transactionsTable;
    }

    /**
     * Metoda pro přidání čísla stránky do zápatí PDF dokumentu.
     *
     * @param writer PdfWriter použitý pro generování PDF dokumentu.
     */
    private void addPageNumberToFooter(PdfWriter writer) {
        // Vytvoření Phrase s textem "Strana [číslo stránky]"
        Phrase pageNumber = new Phrase("Strana " + writer.getPageNumber(), headerFont);

        // Vytvoření PdfPageEvent, který se stará o zobrazení čísla stránky
        PdfPageEvent event = new PdfPageEvent() {
            @Override
            public void onOpenDocument(PdfWriter writer, Document document) {

            }

            @Override
            public void onStartPage(PdfWriter writer, Document document) {

            }

            @Override
            public void onEndPage(PdfWriter writer, Document document) {
                // Získání grafického kontextu pro přímý obsah PDF dokumentu
                PdfContentByte canvas = writer.getDirectContent();

                // Zobrazení čísla stránky zarovnaného na střed zápatí
                ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, pageNumber,
                        (document.right() - document.left()) / 2 + document.leftMargin(),
                        document.bottom() - 10, 0);
            }


            // Tyto metody nejsou využity, ale musí být implementovýny
            @Override
            public void onCloseDocument(PdfWriter writer, Document document) {
                // Tato metoda se vyvolá po uzavření dokumentu; zde by mohly být prováděny další akce.
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

        // Nastavení PdfPageEvent do PdfWriteru, který zajistí, že se číslo stránky přidá na každou stránku dokumentu.
        writer.setPageEvent(event);
    }

    /**
     * Metoda pro vytvoření prázdné tabulky s určeným počtem sloupců.
     *
     * @param numColumns Počet sloupců v tabulce.
     * @return Nová instance PdfPTable.
     */
    private PdfPTable createTable(int numColumns) {
        return new PdfPTable(numColumns);
    }

    /**
     * Metoda pro vytvoření buňky v tabulce s textem, fontem, barvou pozadí a odsazením.
     *
     * @param text           Text, který bude v buňce.
     * @param font           Font pro text v buňce.
     * @param backgroundColor Barva pozadí buňky.
     * @param padding        Odsazení buňky.
     * @return Nová instance PdfPCell.
     */
    private PdfPCell createCell(String text, Font font, BaseColor backgroundColor, float padding) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(0);
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(padding);
        return cell;
    }

    /**
     * Metoda pro vytvoření buňky v tabulce s textem, fontem a barvou pozadí (s výchozím odsazením).
     *
     * @param text           Text, který bude v buňce.
     * @param font           Font pro text v buňce.
     * @param backgroundColor Barva pozadí buňky.
     * @return Nová instance PdfPCell.
     */
    private PdfPCell createCell(String text, Font font, BaseColor backgroundColor) {
        return createCell(text, font, backgroundColor, 2f); // Výchozí hodnota pro odsazení.
    }

    /**
     * Metoda pro přidání buňky s textem, fontem a barvou pozadí do tabulky.
     *
     * @param table          Tabulka, do které má být buňka přidána.
     * @param text           Text, který bude v buňce.
     * @param font           Font pro text v buňce.
     * @param backgroundColor Barva pozadí buňky.
     */
    private void addCell(PdfPTable table, String text, Font font, BaseColor backgroundColor) {
        table.addCell(createCell(text, font, backgroundColor));
    }

    /**
     * Metoda pro odeslání výpisu účtu e-mailem na zadanou adresu.
     *
     * @param recipientEmail E-mailová adresa příjemce.
     */
    private void sendStatementByEmail(String recipientEmail) {
        // Vytvoření detailů e-mailu pro odeslání výpisu účtu.
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(recipientEmail)
                .subject("VÝPIS ÚČTU")
                .messageBody("Váš požadovaný výpis účtu je v příloze!")
                .attachment(FILE) // Připojený soubor (v tomto případě FILE)
                .build();

        // Odeslání e-mailu pomocí služby pro e-mail.
        emailService.sendEmailWithAttachment(emailDetails);
    }
}

