package com.martinPluhar.Bankapplication.util;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXIST_CODE = "001";
    public static final String ACCOUNT_EXIST_MESSAGE = "This user account already exist!";

    public static final String ACCOUNT_CREATION_SUCCESS ="001";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created!";
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
