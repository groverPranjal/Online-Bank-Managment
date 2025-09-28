package com.codeclause.project2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeclause.project2.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByAccountIdAndStatus(Long accountId, String status);
}