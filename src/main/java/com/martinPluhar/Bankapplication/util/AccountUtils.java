package com.martinPluhar.Bankapplication.util;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXIST_CODE = "001";
    public static final String ACCOUNT_EXIST_MESSAGE = "Tento účet již existuje!";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Váš účet byl vytvořen!";
    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "Uživatel s požadovaným číslem účtu neexistuje!";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_SUCCESS = "Uživatel účtu nalezen!";
    public static final String ACCOUNT_CREDITED_SUCCESS = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "Uživatelský účet byl v pořádku načerpán!";
    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Nedostatečný zůstatek!";
    public static final String ACCOUNT_DEBITED_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_MESSAGE = "Výběr proběhl úspěšně!";
    public static final String TRANSFER_SUCCESSFUL_CODE = "008";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Převod proběhl úspěsně!";
    public static final String ACCOUNT_DELETED_CODE = "009";
    public static final String ACCOUNT_DELETED_MESSAGE = "Účet byl smazán";

    public static final String ACCOUNT_LOGIN_SUCCES = "Úspěšné přihlášení";

    /**
     * Generuje náhodné číslo účtu ve formátu: "YYYYNNNNNN/XXXX", kde:
     * - YYYY je aktuální rok
     * - NNNNNN je náhodně vygenerované šestimístné číslo
     * - XXXX je kód banky
     *
     * @return Vygenerované číslo účtu
     */
    public static String generateAccountNumber() {
        // Získání aktuálního roku
        Year currentYear = Year.now();

        // Definice minimálního a maximálního rozsahu pro generování náhodného čísla
        int min = 100000;
        int max = 999999;

        // Kód banky
        String bankCode = "/0100";

        // Generování náhodného čísla v zadaném rozsahu
        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        // Převedení aktuálního roku a náhodného čísla na textový řetězec
        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);

        // Sestavení celého čísla účtu a jeho návrat
        return year + randomNumber + bankCode;
    }
}
