package com.codeclause.project2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.codeclause.project2.model.Account;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testFindByAccountNumber() {
        // Given
        Account account = new Account();
        account.setAccountNumber("ACC123");
        account.setAccountHolderName("John Doe");
        account.setAccountType("Savings");
        account.setBalance(1000.0);
        accountRepository.save(account);

        // When
        List<Account> found = accountRepository.findByAccountNumber("ACC123");

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getAccountHolderName()).isEqualTo("John Doe");
    }

    @Test
    void testFindByAccountHolderName() {
        // Given
        Account account = new Account();
        account.setAccountNumber("ACC123");
        account.setAccountHolderName("John Doe");
        account.setAccountType("Savings");
        account.setBalance(1000.0);
        accountRepository.save(account);

        // When
        List<Account> found = accountRepository.findByAccountHolderName("John Doe");

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getAccountNumber()).isEqualTo("ACC123");
    }
}