package com.codeclause.project2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codeclause.project2.model.Account;
import com.codeclause.project2.service.BankingService;
import com.codeclause.project2.service.TransactionService;

@Controller
public class BankingController {
    
	@Autowired
    private BankingService bankingService;
	@Autowired
	private TransactionService transactionService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title","Home Page");
    	return "home"; 

    }

    @GetMapping("/accounts")
    public String viewAccounts(Model model) {
    	List<Account> accountList = this.bankingService.getAllAccounts();
        model.addAttribute("accounts", accountList);
        model.addAttribute("title", "Accounts");
        return "account"; // Render accounts.html
    }
    @GetMapping("/delete-account")
    public String deleteAccount(@RequestParam Long id) {
        bankingService.deleteAccountById(id); // Implement this in your BankingService
        return "redirect:/accounts"; // Redirect to the accounts page after deletion
    }

    @GetMapping("/add-account")
    public String showAddAccountForm(Model model) {
        model.addAttribute("title", "Add Account");
        return "addAccount"; // This should match the name of your HTML file (addAccount.html)
    }
    @PostMapping("/add-account")
    public String addAccount(@ModelAttribute Account account, Model model) {
        Account newAccount = bankingService.addAccount(account);
        model.addAttribute("message", "Account created successfully!");
        return "redirect:/accounts"; // Redirect to the accounts page after adding
    }

    @GetMapping("/fund-transfer")
    public String viewFundTransfer(Model model) {
        model.addAttribute("accounts", bankingService.getAllAccounts());
        model.addAttribute("title", "Fund Transfer");
        return "fundTransfer"; // Render fundTransfer.html
    }
    @PostMapping("/transfer-funds")
    public String transfer(@RequestParam String fromAccount, @RequestParam String toAccount, @RequestParam double amount, RedirectAttributes redirectAttributes) {
        if (fromAccount.equals(toAccount)) {
            redirectAttributes.addFlashAttribute("message", "Both accounts cannot be the same!!");
            return "redirect:/fund-transfer";
        }
        if(bankingService.transferFunds(fromAccount, toAccount, amount))
        	redirectAttributes.addFlashAttribute("message", "Transfer successful!");
        else
        	redirectAttributes.addFlashAttribute("message", "Transfer failed!");
        return "redirect:/transaction-history"; 
    }


    @GetMapping("/transaction-history")
    public String viewTransactionHistory(Model model) {
        model.addAttribute("transactions", transactionService.getTransactionHistory());
        model.addAttribute("title", "Transaction History");
        return "transactionHistory"; // Render transactionHistory.html
    }
    @GetMapping("/delete-transaction/{id}")
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