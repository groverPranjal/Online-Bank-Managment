package com.codeclause.project2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeclause.project2.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String accountNumber);
}