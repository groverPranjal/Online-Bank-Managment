package com.codeclause.project2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codeclause.project2.model.Account;
import com.codeclause.project2.model.User;
import com.codeclause.project2.repository.UserRepository;
import com.codeclause.project2.service.BankingService;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankingService bankingService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // returns login.html from templates
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String username, @RequestParam String password,
            @RequestParam String confirmPassword, Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("message", "Passwords do not match");
            return "register";
        }
        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("message", "Username already exists");
            return "register";
        }
        // Create user
        User user = new User(name, username, passwordEncoder.encode(password), "USER");
        userRepository.save(user);

        // Create account
        String accountNumber = "AC" + System.currentTimeMillis();
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountHolderName(username);
        account.setAccountType("Savings");
        account.setBalance(0.0);
        bankingService.addAccount(account);

        return "redirect:/login?registered";
    }
}
