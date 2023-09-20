package com.martinPluhar.Bankapplication.services.impl;

import com.martinPluhar.Bankapplication.dto.BankResponse;
import com.martinPluhar.Bankapplication.dto.CreditDebitRequest;
import com.martinPluhar.Bankapplication.dto.EnquiryRequest;
import com.martinPluhar.Bankapplication.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
}
