package com.martinPluhar.Bankapplication.services.impl;

import com.martinPluhar.Bankapplication.dto.TransactionDto;
import com.martinPluhar.Bankapplication.entity.Transaction;
import com.martinPluhar.Bankapplication.repository.TransactionRepository;
import com.martinPluhar.Bankapplication.services.intfc.TransactionService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementace rozhraní {@link TransactionService} pro ukládání transakcí do databáze.
 * Tato třída zajišťuje ukládání informací o transakcích, včetně typu transakce, částky, odesílatele,
 * příjemce a stavu transakce (zda byla úspěšně provedena).
 */
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Metoda pro uložení informací o transakci do databáze.
     *
     * @param transactionDto  Objekt obsahující informace o transakci (typ, částka).
     * @param senderAccount   Číslo účtu odesílatele transakce.
     * @param receiverAccount Číslo účtu příjemce transakce.
     */
    @Override
    public void saveTransaction(TransactionDto transactionDto, String senderAccount, String receiverAccount) {
        // Vytvoření objektu transakce s potřebnými informacemi
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .amount(transactionDto.getAmount())
                .senderAccount(senderAccount)
                .receiverAccount(receiverAccount)
                .status("SUCESS") // Předpokládáme, že transakce byla úspěšně provedena
                .build();

        // Uložení transakce do databáze
        transactionRepository.save(transaction);
    }
}



