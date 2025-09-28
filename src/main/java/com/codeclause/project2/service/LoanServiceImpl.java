package com.codeclause.project2.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import com.codeclause.project2.model.Account;
import com.codeclause.project2.model.Loan;
import com.codeclause.project2.model.Transaction;
import com.codeclause.project2.repository.AccountRepository;
import com.codeclause.project2.repository.LoanRepository;
import com.codeclause.project2.repository.TransactionRepository;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public Loan saveLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    public void approveLoan(Long id) {
        Loan loan = getLoanById(id);
        if (loan != null) {
            loan.setStatus("approved");
            // Calculate EMI
            double P = loan.getAmount();
            double annualRate = loan.getInterestRate();
            double r = annualRate / 12 / 100; // monthly rate
            int n = loan.getDurationMonths();
            double emi = P * r * Math.pow(1 + r, n) / (Math.pow(1 + r, n) - 1);
            loan.setEmiAmount(emi);
            loan.setRemainingBalance(P);
            loanRepository.save(loan);

            // Add loan amount to account balance
            Account account = accountRepository.findById(loan.getAccountId()).orElse(null);
            if (account != null) {
                account.setBalance(account.getBalance() + P);
                accountRepository.save(account);
            }
        }
    }

    @Override
    public void rejectLoan(Long id, String reason) {
        Loan loan = getLoanById(id);
        if (loan != null) {
            loan.setStatus("rejected");
            loan.setRejectionReason(reason);
            loanRepository.save(loan);
        }
    }

    @Override
    public void makeLoanPayment(Long loanId, Long accountId, double amount) throws Exception {
        Loan loan = getLoanById(loanId);
        Account account = accountRepository.findById(accountId).orElse(null);

        if (loan == null) {
            throw new Exception("Loan not found");
        }
        if (account == null) {
            throw new Exception("Account not found");
        }
        if (!"approved".equals(loan.getStatus())) {
            throw new Exception("Loan is not approved");
        }
        if (account.getBalance() < amount) {
            throw new Exception("Insufficient balance");
        }
        if (amount > loan.getRemainingBalance()) {
            throw new Exception("Payment amount exceeds remaining balance");
        }

        // Deduct from account
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        // Update loan remaining balance
        loan.setRemainingBalance(loan.getRemainingBalance() - amount);
        loanRepository.save(loan);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account.getAccountNumber());
        transaction.setToAccount("LOAN_PAYMENT_LOAN_" + loan.getId());
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus(true);
        transactionRepository.save(transaction);
    }
}