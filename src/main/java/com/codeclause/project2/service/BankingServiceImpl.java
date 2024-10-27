package com.codeclause.project2.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public boolean transferFunds(String fromAccount, String toAccount, double amount) {
        Account fromAcc = accountRepository.findByAccountNumber(fromAccount);
        Account toAcc = accountRepository.findByAccountNumber(toAccount);
        boolean transfer=true;
        if (fromAcc != null && toAcc != null && fromAcc.getBalance() >= amount) {
            fromAcc.setBalance(fromAcc.getBalance() - amount);
            toAcc.setBalance(toAcc.getBalance() + amount);
            accountRepository.save(fromAcc);
            accountRepository.save(toAcc);
        }
        else {
        	transfer=false;
        }
        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus(transfer);
        transactionRepository.save(transaction);
        return transfer;
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
