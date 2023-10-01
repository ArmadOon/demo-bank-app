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

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TransactionImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transactionDto, String senderAccount, String receiverAccount) {

        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .amount(transactionDto.getAmount())
                .senderAccount(senderAccount)
                .receiverAccount(receiverAccount)
                .status("SUCCESS")
                .build();
        transactionRepository.save(transaction);


    }
}



