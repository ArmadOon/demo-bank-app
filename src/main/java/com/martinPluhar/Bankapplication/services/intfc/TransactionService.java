package com.martinPluhar.Bankapplication.services.intfc;

import com.martinPluhar.Bankapplication.entity.Transaction;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
}
