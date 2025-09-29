package com.codeclause.project2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codeclause.project2.model.Account;
import com.codeclause.project2.model.Loan;
import com.codeclause.project2.repository.AccountRepository;
import com.codeclause.project2.repository.LoanRepository;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/dashboard")
    public String userDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Account> userAccounts = accountRepository.findByAccountHolderName(username);
        if (!userAccounts.isEmpty()) {
            Long accountId = userAccounts.get(0).getId(); // Assuming one account per user
            List<Loan> approvedLoans = loanRepository.findByAccountIdAndStatus(accountId, "approved");
            if (!approvedLoans.isEmpty()) {
                Loan loan = approvedLoans.get(0); // Assuming one loan
                model.addAttribute("hasLoan", true);
                model.addAttribute("loanEmi", loan.getEmiAmount());
                model.addAttribute("remainingBalance", loan.getRemainingBalance());
            } else {
                model.addAttribute("hasLoan", false);
            }
        }
        return "user/dashboard";
    }

}