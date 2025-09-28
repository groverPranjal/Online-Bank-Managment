package com.codeclause.project2.service;

import java.util.List;
import com.codeclause.project2.model.Loan;

public interface LoanService {
    List<Loan> getAllLoans();
    Loan getLoanById(Long id);
    Loan saveLoan(Loan loan);
    void approveLoan(Long id);
    void rejectLoan(Long id, String reason);
    void makeLoanPayment(Long loanId, Long accountId, double amount) throws Exception;
}