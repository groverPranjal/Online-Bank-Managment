package com.codeclause.project2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeclause.project2.model.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccount(String accountNumber);
}