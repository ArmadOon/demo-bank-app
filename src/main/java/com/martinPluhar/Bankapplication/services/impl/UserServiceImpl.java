package com.martinPluhar.Bankapplication.services.impl;

import com.martinPluhar.Bankapplication.dto.BankResponse;
import com.martinPluhar.Bankapplication.dto.UserRequest;
import com.martinPluhar.Bankapplication.entity.User;
import com.martinPluhar.Bankapplication.util.AccountUtils;

import java.math.BigDecimal;

public class UserServiceImpl implements UserService{

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /**
         * Creating an acc - saving a new user into database
         */
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .middleName(userRequest.getMiddleName())
                .gander(userRequest.getGander())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("Active")
                .build();


    }
}
