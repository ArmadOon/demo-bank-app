package com.martinPluhar.Bankapplication.services.impl;

import com.martinPluhar.Bankapplication.dto.BankResponse;
import com.martinPluhar.Bankapplication.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
}
