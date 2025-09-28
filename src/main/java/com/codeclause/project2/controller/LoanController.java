 package com.codeclause.project2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codeclause.project2.model.Account;
import com.codeclause.project2.model.Loan;
import com.codeclause.project2.service.BankingService;
import com.codeclause.project2.service.LoanService;

@Controller
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private BankingService bankingService;

    @GetMapping("/loan-approval")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewLoanApproval(Model model) {
        model.addAttribute("loans", loanService.getAllLoans());
        model.addAttribute("title", "Loan Approval");
        return "loanApproval";
    }

    @PostMapping("/approve-loan")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveLoan(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        loanService.approveLoan(id);
        redirectAttributes.addFlashAttribute("message", "Loan approved successfully!");
        return "redirect:/loan-approval";
    }

    @PostMapping("/reject-loan")
    @PreAuthorize("hasRole('ADMIN')")
    public String rejectLoan(@RequestParam Long id, @RequestParam String rejectionReason, RedirectAttributes redirectAttributes) {
        loanService.rejectLoan(id, rejectionReason);
        redirectAttributes.addFlashAttribute("message", "Loan rejected successfully!");
        return "redirect:/loan-approval";
    }

    @GetMapping("/loan-request")
    @PreAuthorize("hasRole('USER')")
    public String viewLoanRequest(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Account> userAccounts = bankingService.getAllAccounts().stream()
            .filter(a -> a.getAccountHolderName().equals(username))
            .collect(java.util.stream.Collectors.toList());
        model.addAttribute("accounts", userAccounts);
        model.addAttribute("title", "Loan Request");
        return "loanRequest";
    }

    @PostMapping("/loan-request")
    @PreAuthorize("hasRole('USER')")
    public String submitLoanRequest(@ModelAttribute Loan loan, @RequestParam Long accountId, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        loan.setApplicantName(username);
        loan.setAccountId(accountId);
        loan.setInterestRate(5.0); // Fixed interest rate of 5%
        loan.setStatus("pending");
        loanService.saveLoan(loan);
        redirectAttributes.addFlashAttribute("message", "Loan request submitted successfully!");
        return "redirect:/loan-request";
    }

    @GetMapping("/loan-payment")
    @PreAuthorize("hasRole('USER')")
    public String viewLoanPayment(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Account> userAccounts = bankingService.getAllAccounts().stream()
            .filter(a -> a.getAccountHolderName().equals(username))
            .collect(java.util.stream.Collectors.toList());
        List<Loan> approvedLoans = loanService.getAllLoans().stream()
            .filter(l -> l.getApplicantName().equals(username) && "approved".equals(l.getStatus()))
            .collect(java.util.stream.Collectors.toList());
        model.addAttribute("accounts", userAccounts);
        model.addAttribute("loans", approvedLoans);
        model.addAttribute("title", "Loan Payment");
        return "loanPayment";
    }

    @PostMapping("/loan-payment")
    @PreAuthorize("hasRole('USER')")
    public String makeLoanPayment(@RequestParam Long loanId, @RequestParam Long accountId, @RequestParam double amount, RedirectAttributes redirectAttributes) {
        try {
            loanService.makeLoanPayment(loanId, accountId, amount);
            redirectAttributes.addFlashAttribute("message", "Loan payment successful!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Payment failed: " + e.getMessage());
        }
        return "redirect:/loan-payment";
    }
}