package com.martinPluhar.Bankapplication.repository;

import com.martinPluhar.Bankapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


//? This repository help us to manipulate database field User
public interface UserRepository extends JpaRepository<User,Long> {
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);
}
