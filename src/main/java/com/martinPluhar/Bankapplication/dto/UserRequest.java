package com.martinPluhar.Bankapplication.dto;

import com.martinPluhar.Bankapplication.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String role;
    private String firstName;
    private String lastName;
    private String anotherName;
    private String gander;
    private String address;
    private String stateOfOrigin;
    private String email;
    private String phoneNumber;
    private String alternativePhoneNumber;
    private String password;
}
