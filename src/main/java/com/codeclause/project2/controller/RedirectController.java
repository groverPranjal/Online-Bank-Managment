package com.codeclause.project2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/redirect")
    public String redirectAfterLogin(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (AuthorityUtils.authorityListToSet(authentication.getAuthorities()).contains("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard";
            } else if (AuthorityUtils.authorityListToSet(authentication.getAuthorities()).contains("ROLE_USER")) {
                return "redirect:/user/dashboard";
            }
        }
        return "redirect:/login";
    }
}