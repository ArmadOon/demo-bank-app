package com.martinPluhar.Bankapplication.services.impl;

import com.martinPluhar.Bankapplication.entity.Transaction;
import com.martinPluhar.Bankapplication.services.intfc.TransactionService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionImpl implements TransactionService {

    @Override
    public void saveTransaction(Transaction transaction) {

    }
}
