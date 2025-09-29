package com.codeclause.project2.service;

import java.util.List;

import com.codeclause.project2.model.Account;

public interface BankingService {
	 public String transferFunds(String fromAccount, String toAccount, double amount);

	public Account addAccount(Account account);

	public List<Account> getAllAccounts();

	public void deleteAccountById(Long id);

	public String deposit(String accountNumber, double amount);
}
