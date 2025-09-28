package com.codeclause.project2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeclause.project2.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByAccountNumber(String accountNumber);
    List<Account> findByAccountHolderName(String accountHolderName);
}