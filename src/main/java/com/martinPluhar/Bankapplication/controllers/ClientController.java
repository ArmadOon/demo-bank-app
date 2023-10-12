package com.martinPluhar.Bankapplication.controllers;

import com.martinPluhar.Bankapplication.dto.*;
import com.martinPluhar.Bankapplication.repository.UserRepository;
import com.martinPluhar.Bankapplication.services.intfc.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Client management APIs")
@RestController
@RequestMapping("/api/user")
public class ClientController {
    @Autowired
    UserService userService;
    UserRepository userRepository;

    @Operation(
            summary = "Create new client account",
            description = "Creating new client and assign account id and generate random account number"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status code 201 created"

    )
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @PostMapping("/login")
    public BankResponse login(@RequestBody LoginDto loginDto){

        return userService.login(loginDto);
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Given an account number "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status code 200 SUCCESS"

    )
    @GetMapping("balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
        return userService.balanceEnquiry(request);
    }

    @GetMapping("nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request) {
        return userService.nameEnquiry(request);
    }

    @PostMapping("credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
        return userService.creditAccount(request);
    }

    @PostMapping("debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
        return userService.debitAccount(request);
    }

    @PostMapping("transfer")
    public BankResponse transfer(@RequestBody TransferRequest request) {
        return userService.transfer(request);
    }

    @DeleteMapping("delete")
    public BankResponse deleteAccountByEmail(@RequestParam String email) {
        return userService.deleteAccountByEmail(email);

    }
}
