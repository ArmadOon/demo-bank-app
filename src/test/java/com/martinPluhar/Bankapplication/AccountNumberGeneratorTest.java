package com.martinPluhar.Bankapplication;
import static org.junit.Assert.*;

import com.martinPluhar.Bankapplication.util.AccountUtils;
import org.junit.Test;
public class AccountNumberGeneratorTest {

    @Test
    public void testGenerateAccountNumber() {
        // Arrange
        String expectedPattern = "^2023\\d{6}/0100$"; // Očekávaný vzor pro číslo účtu

        // Act
        String accountNumber = AccountUtils.generateAccountNumber();

        // Assert
        assertNotNull(accountNumber);
        assertTrue(accountNumber.matches(expectedPattern));

    }
}
