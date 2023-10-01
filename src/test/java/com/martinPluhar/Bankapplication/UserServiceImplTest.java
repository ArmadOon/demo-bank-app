package com.martinPluhar.Bankapplication;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.martinPluhar.Bankapplication.dto.*;
import com.martinPluhar.Bankapplication.entity.User;
import com.martinPluhar.Bankapplication.repository.UserRepository;
import com.martinPluhar.Bankapplication.services.impl.UserServiceImpl;
import com.martinPluhar.Bankapplication.services.intfc.EmailService;
import com.martinPluhar.Bankapplication.services.intfc.TransactionService;
import com.martinPluhar.Bankapplication.util.AccountUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateAccount_Success() {
        // Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("John");
        userRequest.setLastName("Doe");
        userRequest.setEmail("john.doe@example.com");
        // Mockování chování userRepository pro existenci e-mailu
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
        // Mockování chování userRepository pro uložení nového uživatele
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User newUser = invocation.getArgument(0);
            newUser.setId(1L); // Simulace přiřazení ID po uložení
            return newUser;
        });

        // Act
        BankResponse response = userService.createAccount(userRequest);

        // Assert
        assertNotNull(response);
        assertEquals(AccountUtils.ACCOUNT_CREATION_SUCCESS, response.getResponseCode());
        assertNotNull(response.getAccountInfo());
        assertEquals("John Doe ", response.getAccountInfo().getAccountName());
        assertEquals(BigDecimal.ZERO, response.getAccountInfo().getAccountBalance());
    }

    @Test
    public void testCreateAccount_AccountExists() {
        // Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("existing@example.com");
        // Mockování chování userRepository pro existenci e-mailu
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(true);

        // Act
        BankResponse response = userService.createAccount(userRequest);

        // Assert
        assertNotNull(response);
        assertEquals(AccountUtils.ACCOUNT_EXIST_CODE, response.getResponseCode());
        assertNull(response.getAccountInfo());

    }


    @Test
    public void testTransfer_DestinationAccountNotExist() {
        // Arrange
        TransferRequest request = new TransferRequest();
        request.setSourceAccountNumber("sourceAccount");
        request.setDestinationAccountNumber("nonExistentAccount");
        request.setAmount(BigDecimal.valueOf(100));

        // Mockování chování userRepository pro neexistující cílový účet
        when(userRepository.existsByAccountNumber(request.getDestinationAccountNumber())).thenReturn(false);

        // Act
        BankResponse response = userService.transfer(request);

        // Assert
        assertNotNull(response);
        assertEquals(AccountUtils.ACCOUNT_NOT_EXIST_CODE, response.getResponseCode());
    }


}


