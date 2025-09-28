package com.codeclause.project2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codeclause.project2.model.Account;
import com.codeclause.project2.repository.AccountRepository;
import com.codeclause.project2.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class BankingServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BankingServiceImpl bankingService;

    @Test
    void testAddAccount() {
        // Given
        Account account = new Account();
        account.setAccountNumber("ACC123");
        account.setAccountHolderName("John Doe");
        account.setAccountType("Savings");
        account.setBalance(1000.0);

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // When
        Account saved = bankingService.addAccount(account);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getAccountNumber()).isEqualTo("ACC123");
    }

    @Test
    void testGetAllAccounts() {
        // Given
        Account account1 = new Account();
        account1.setAccountNumber("ACC123");
        Account account2 = new Account();
        account2.setAccountNumber("ACC456");

        when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));

        // When
        List<Account> accounts = bankingService.getAllAccounts();

        // Then
        assertThat(accounts).hasSize(2);
    }

    @Test
    void testTransferFundsSuccess() {
        // Given
        Account fromAccount = new Account();
        fromAccount.setId(1L);
        fromAccount.setAccountNumber("ACC123");
        fromAccount.setBalance(1000.0);

        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setAccountNumber("ACC456");
        toAccount.setBalance(500.0);

        when(accountRepository.findByAccountNumber("ACC123")).thenReturn(Arrays.asList(fromAccount));
        when(accountRepository.findByAccountNumber("ACC456")).thenReturn(Arrays.asList(toAccount));

        // When
        String result = bankingService.transferFunds("ACC123", "ACC456", 200.0);

        // Then
        assertThat(result).isEqualTo("Transfer successful!");
        assertThat(fromAccount.getBalance()).isEqualTo(800.0);
        assertThat(toAccount.getBalance()).isEqualTo(700.0);
    }

    @Test
    void testTransferFundsInsufficientBalance() {
        // Given
        Account fromAccount = new Account();
        fromAccount.setAccountNumber("ACC123");
        fromAccount.setBalance(100.0);

        Account toAccount = new Account();
        toAccount.setAccountNumber("ACC456");
        toAccount.setBalance(500.0);

        when(accountRepository.findByAccountNumber("ACC123")).thenReturn(Arrays.asList(fromAccount));
        when(accountRepository.findByAccountNumber("ACC456")).thenReturn(Arrays.asList(toAccount));

        // When
        String result = bankingService.transferFunds("ACC123", "ACC456", 200.0);

        // Then
        assertThat(result).isEqualTo("Insufficient balance!");
    }
}