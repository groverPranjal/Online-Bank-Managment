package com.codeclause.project2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codeclause.project2.model.Transaction;
import com.codeclause.project2.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public List<Transaction> getTransactionHistory() {
		return this.transactionRepository.findAll();
	}

	@Override
	public boolean deleteTransactionById(Long id) {
		if (transactionRepository.existsById(id)) {
	        transactionRepository.deleteById(id);
	        return true;
	    }
	    return false;
	}
}
