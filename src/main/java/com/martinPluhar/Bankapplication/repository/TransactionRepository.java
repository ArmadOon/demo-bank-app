package com.martinPluhar.Bankapplication.repository;

import com.martinPluhar.Bankapplication.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,String> {
}
