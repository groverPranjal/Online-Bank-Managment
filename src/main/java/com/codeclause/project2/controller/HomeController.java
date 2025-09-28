package com.codeclause.project2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.codeclause.project2.model.Account;
import com.codeclause.project2.model.Loan;
import com.codeclause.project2.repository.AccountRepository;
import com.codeclause.project2.repository.LoanRepository;
import com.codeclause.project2.repository.UserRepository;
import com.codeclause.project2.service.BankingService;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BankingService bankingService;

    @GetMapping("/home")
    public String homePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isUser = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
        if (isAdmin) {
            long userCount = userRepository.count();
            long loanCount = loanRepository.count();
            List<Account> accounts = bankingService.getAllAccounts();
            double totalBalance = accounts.stream().mapToDouble(Account::getBalance).sum();
            model.addAttribute("userCount", userCount);
            model.addAttribute("loanCount", loanCount);
            model.addAttribute("totalBalance", totalBalance);
        }
        if (isUser) {
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
        }
        return "home"; // returns home.html from templates
    }
}
