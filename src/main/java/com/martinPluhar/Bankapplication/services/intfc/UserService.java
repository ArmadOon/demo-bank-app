package com.martinPluhar.Bankapplication.services.intfc;

import com.martinPluhar.Bankapplication.dto.*;
import org.hibernate.engine.jdbc.batch.spi.BatchKey;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transfer(TransferRequest request);
    BankResponse deleteAccountByEmail(String email);
}
