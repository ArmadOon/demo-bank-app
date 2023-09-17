package com.martinPluhar.Bankapplication.util;

import java.time.Year;

public class AccountUtils {

    public static String generateAccountNumber() {

        /*
          2023 + random six digits
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
