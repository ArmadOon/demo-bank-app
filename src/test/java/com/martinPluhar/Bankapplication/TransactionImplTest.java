package com.martinPluhar.Bankapplication;

import com.martinPluhar.Bankapplication.dto.TransactionDto;
import com.martinPluhar.Bankapplication.entity.Transaction;
import com.martinPluhar.Bankapplication.repository.TransactionRepository;
import com.martinPluhar.Bankapplication.services.impl.TransactionImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

public class TransactionImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionImpl transactionService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveTransaction_Success() {
        // Arrange
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType("Credit");
        transactionDto.setAmount(BigDecimal.valueOf(100.0)); // Použijeme BigDecimal

        String senderAccount = "sender123";
        String receiverAccount = "receiver456";

        // Mockování chování TransactionRepository pro metodu save
        Transaction savedTransaction = new Transaction(); // Vytvoříme instanci Transaction, kterou metoda save vrátí
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        // Act
        transactionService.saveTransaction(transactionDto, senderAccount, receiverAccount);

        // Assert
        verify(transactionRepository, times(1)).save(argThat(transaction -> {
            // Ověření, zda byl objekt Transaction správně vytvořen a nastaven
            return "Credit".equals(transaction.getTransactionType())
                    && BigDecimal.valueOf(100.0).compareTo(transaction.getAmount()) == 0 // Porovnání pomocí compareTo
                    && "sender123".equals(transaction.getSenderAccount())
                    && "receiver456".equals(transaction.getReceiverAccount())
                    && "SUCCESS".equals(transaction.getStatus());
        }));
    }
    @Test
    public void testSaveTransaction_Credit_Success() {
        // Arrange
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType("Credit");
        transactionDto.setAmount(BigDecimal.valueOf(100.0));

        String senderAccount = "sender123";
        String receiverAccount = "receiver456";

        // Mockování chování TransactionRepository pro metodu save
        Transaction savedTransaction = new Transaction();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        // Act
        transactionService.saveTransaction(transactionDto, senderAccount, receiverAccount);

        // Assert
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        // Další ověření, pokud je potřeba
    }

    @Test
    public void testSaveTransaction_Debit_Success() {
        // Arrange
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType("Debit");
        transactionDto.setAmount(BigDecimal.valueOf(50.0));

        String senderAccount = "sender789";
        String receiverAccount = "receiver101";

        // Mockování chování TransactionRepository pro metodu save
        Transaction savedTransaction = new Transaction();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        // Act
        transactionService.saveTransaction(transactionDto, senderAccount, receiverAccount);

        // Assert
        verify(transactionRepository, times(1)).save(any(Transaction.class));

    }

}