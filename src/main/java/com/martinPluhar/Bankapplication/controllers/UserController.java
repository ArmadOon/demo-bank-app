package com.martinPluhar.Bankapplication.controllers;

import com.martinPluhar.Bankapplication.dto.BankResponse;
import com.martinPluhar.Bankapplication.dto.UserRequest;
import com.martinPluhar.Bankapplication.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

}
