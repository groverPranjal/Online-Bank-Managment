package com.codeclause.project2.service;

import java.util.List;

import com.codeclause.project2.model.Transaction;

public interface TransactionService {

	List<Transaction> getTransactionHistory();

	boolean deleteTransactionById(Long id);

}
