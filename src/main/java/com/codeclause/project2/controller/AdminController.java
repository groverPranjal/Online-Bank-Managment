package com.codeclause.project2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codeclause.project2.model.Account;
// import com.codeclause.project2.model.Loan;
import com.codeclause.project2.repository.AccountRepository;
import com.codeclause.project2.repository.LoanRepository;
import com.codeclause.project2.repository.UserRepository;
import com.codeclause.project2.service.BankingService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BankingService bankingService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        long accountCount = accountRepository.count();
        long loanCount = loanRepository.count();
        List<Account> accounts = bankingService.getAllAccounts();
        double totalBalance = accounts.stream().mapToDouble(Account::getBalance).sum();
        model.addAttribute("accountCount", accountCount);
        model.addAttribute("loanCount", loanCount);
        model.addAttribute("totalBalance", totalBalance);
        return "admin/dashboard";
    }

    @GetMapping("/deposit")
    public String viewDeposit(Model model) {
        List<Account> accounts = bankingService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "deposit";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam double amount, Model model) {
        String message = bankingService.deposit(accountNumber, amount);
        model.addAttribute("message", message);
        List<Account> accounts = bankingService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "deposit";
    }


}