package com.codeclause.project2.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codeclause.project2.model.Account;
import com.codeclause.project2.model.Transaction;
import com.codeclause.project2.service.BankingService;
import com.codeclause.project2.service.TransactionService;

@Controller
public class BankingController {
    
	@Autowired
    private BankingService bankingService;
	@Autowired
	private TransactionService transactionService;

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public String index(Model model) {
        model.addAttribute("title","Home Page");
    	return "home";

    }

    @GetMapping("/accounts")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewAccounts(Model model) {
    	List<Account> accountList = this.bankingService.getAllAccounts();
        model.addAttribute("accounts", accountList);
        model.addAttribute("title", "Accounts");
        return "account"; // Render accounts.html
    }

    @GetMapping("/my-accounts")
    @PreAuthorize("hasRole('USER')")
    public String viewMyAccounts(Model model) {
        List<Account> allAccounts = this.bankingService.getAllAccounts();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Account> myAccounts = allAccounts.stream().filter(a -> a.getAccountHolderName().equals(username)).collect(Collectors.toList());
        model.addAttribute("accounts", myAccounts);
        model.addAttribute("title", "My Accounts");
        return "account"; 
    }
    @GetMapping("/delete-account")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteAccount(@RequestParam Long id) {
        bankingService.deleteAccountById(id);
        return "redirect:/accounts"; 
    }

    @GetMapping("/add-account")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddAccountForm(Model model) {
        model.addAttribute("title", "Add Account");
        return "addAccount"; // This should match the name of your HTML file (addAccount.html)
    }
    @PostMapping("/add-account")
    @PreAuthorize("hasRole('ADMIN')")
    public String addAccount(@ModelAttribute Account account, Model model) {
        // Generate unique account number
        String accountNumber = "AC" + System.currentTimeMillis();
        account.setAccountNumber(accountNumber);
        Account newAccount = bankingService.addAccount(account);
        model.addAttribute("message", "Account created successfully with Account Number: " + accountNumber);
        return "redirect:/accounts"; // Redirect to the accounts page after adding
    }

    @GetMapping("/fund-transfer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public String viewFundTransfer(Model model) {
        List<Account> allAccounts = bankingService.getAllAccounts();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isUser = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
        if (isUser) {
            List<Account> fromAccounts = allAccounts.stream().filter(a -> a.getAccountHolderName().equals(username)).collect(Collectors.toList());
            List<Account> toAccounts = allAccounts.stream().filter(a -> !a.getAccountHolderName().equals(username)).collect(Collectors.toList());
            model.addAttribute("fromAccounts", fromAccounts);
            model.addAttribute("toAccounts", toAccounts);
        } else {
            model.addAttribute("fromAccounts", allAccounts);
            model.addAttribute("toAccounts", allAccounts);
        }
        model.addAttribute("title", "Fund Transfer");
        return "fundTransfer"; // Render fundTransfer.html
    }
    @PostMapping("/transfer-funds")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public String transfer(@RequestParam String fromAccount, @RequestParam String toAccount, @RequestParam double amount, RedirectAttributes redirectAttributes) {
        if (fromAccount.equals(toAccount)) {
            redirectAttributes.addFlashAttribute("message", "Both accounts cannot be the same!!");
            return "redirect:/fund-transfer";
        }
        String result = bankingService.transferFunds(fromAccount, toAccount, amount);
        redirectAttributes.addFlashAttribute("message", result);
        return "redirect:/transaction-history";
    }


    @GetMapping("/transaction-history")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public String viewTransactionHistory(@RequestParam(required = false) String accountNumber, Model model) {
        List<Transaction> transactions = transactionService.getTransactionHistory();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isUser = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
        List<Account> userAccounts = null;
        if (isUser) {
            userAccounts = bankingService.getAllAccounts().stream()
                .filter(a -> a.getAccountHolderName().equals(username))
                .collect(Collectors.toList());
            List<String> userAccountNumbers = userAccounts.stream()
                .map(Account::getAccountNumber)
                .collect(Collectors.toList());
            if (accountNumber != null && !accountNumber.isEmpty()) {
                // Filter to specific account
                transactions = transactions.stream()
                    .filter(t -> accountNumber.equals(t.getFromAccount()) || accountNumber.equals(t.getToAccount()))
                    .collect(Collectors.toList());
            } else {
                // Filter to all user accounts
                transactions = transactions.stream()
                    .filter(t -> userAccountNumbers.contains(t.getFromAccount()) || userAccountNumbers.contains(t.getToAccount()))
                    .collect(Collectors.toList());
            }
        }
        model.addAttribute("transactions", transactions);
        if (isUser) {
            model.addAttribute("userAccounts", userAccounts);
            model.addAttribute("selectedAccount", accountNumber);
        }
        model.addAttribute("title", "Transaction History");
        return "transactionHistory"; // Render transactionHistory.html
    }
    @GetMapping("/delete-transaction/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTransaction(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean isDeleted = transactionService.deleteTransactionById(id);

        if (isDeleted) {
            redirectAttributes.addFlashAttribute("success", "Transaction deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Transaction deletion failed.");
        }
        return "redirect:/transaction-history";
    }

    
}