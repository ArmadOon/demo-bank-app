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
import org.springframework.stereotype.Service;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("SUCCESS")
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transakce byla uložena!");
    }
}
