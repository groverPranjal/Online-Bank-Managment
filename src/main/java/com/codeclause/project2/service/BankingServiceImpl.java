package com.codeclause.project2.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeclause.project2.model.Account;
import com.codeclause.project2.model.Transaction;
import com.codeclause.project2.repository.AccountRepository;
import com.codeclause.project2.repository.TransactionRepository;

@Service
public class BankingServiceImpl implements BankingService{
	@Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public String transferFunds(String fromAccount, String toAccount, double amount) {
        List<Account> fromAccs = accountRepository.findByAccountNumber(fromAccount);
        List<Account> toAccs = accountRepository.findByAccountNumber(toAccount);
        String message = "Transfer successful!";
        Account fromAcc = null;
        Account toAcc = null;
        if (fromAccs.size() == 1 && toAccs.size() == 1) {
            fromAcc = fromAccs.get(0);
            toAcc = toAccs.get(0);
        } else {
            message = "Invalid account numbers!";
        }
        if (fromAcc != null && toAcc != null) {
            if (fromAcc.getBalance() >= amount) {
                fromAcc.setBalance(fromAcc.getBalance() - amount);
                toAcc.setBalance(toAcc.getBalance() + amount);
                accountRepository.save(fromAcc);
                accountRepository.save(toAcc);
            } else {
                message = "Insufficient balance!";
            }
        }
        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus(message.equals("Transfer successful!"));
        transactionRepository.save(transaction);
        return message;
    }

	@Override
	public Account addAccount(Account account) {
		return this.accountRepository.save(account);
	}

	@Override
	public List<Account> getAllAccounts() {
		return this.accountRepository.findAll();
	}

	@Override
	public void deleteAccountById(Long id) {
		this.accountRepository.deleteById(id);
		
	}
}
