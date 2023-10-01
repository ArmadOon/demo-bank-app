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

    public static String generateAccountNumber() {

        /*
          2023 + random six digits + bank code
          example "202312878/0100"
         */

        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;
        String bankCode = "/0100";
        //* generate a random number between mind and max

        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
        //* convert the current and randomNumber to strings, then concatenate

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);

        return year + randomNumber + bankCode;
    }
}
