package com.martinPluhar.Bankapplication.services.impl;

import com.martinPluhar.Bankapplication.dto.AccountInfo;
import com.martinPluhar.Bankapplication.dto.BankResponse;
import com.martinPluhar.Bankapplication.dto.EmailDetails;
import com.martinPluhar.Bankapplication.dto.UserRequest;
import com.martinPluhar.Bankapplication.entity.User;
import com.martinPluhar.Bankapplication.repository.UserRepository;
import com.martinPluhar.Bankapplication.util.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /**
         * Creating an acc - saving a new user into database.
         * Check if user already has an account
         */

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .anotherName(userRequest.getAnotherName())
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

        User savedUser = userRepository.save(newUser);

        // Send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account creation")
                .messageBody("Congratulations and welcome! Your account was successfully created!\n" +
                        "Your accounts details: \n" +
                        "Account name:" + savedUser.getFirstName() + " "
                        + savedUser.getLastName() + " "
                        + savedUser.getAnotherName() + "\nAccount number:" + savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getAnotherName())
                        .build())
                .build();
    }
}
