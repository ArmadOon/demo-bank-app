package com.martinPluhar.Bankapplication.services.intfc;

import com.martinPluhar.Bankapplication.dto.TransactionDto;
import com.martinPluhar.Bankapplication.entity.Transaction;

public interface TransactionService {
     void saveTransaction(TransactionDto transactionDto, String senderAccount, String receiverAccount);


}
