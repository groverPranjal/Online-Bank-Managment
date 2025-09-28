package com.codeclause.project2.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.codeclause.project2.model.Account;
import com.codeclause.project2.model.User;
import com.codeclause.project2.repository.UserRepository;
import com.codeclause.project2.service.BankingService;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BankingService bankingService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void testRegisterSuccess() throws Exception {
        // Given
        when(userRepository.findByUsername("johndoe")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(bankingService.addAccount(any(Account.class))).thenReturn(new Account());

        // When & Then
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("name", "John Doe")
                .param("username", "johndoe")
                .param("password", "password")
                .param("confirmPassword", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));
    }

    @Test
    void testRegisterPasswordsDoNotMatch() throws Exception {
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("name", "John Doe")
                .param("username", "johndoe")
                .param("password", "password")
                .param("confirmPassword", "different"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("message", "Passwords do not match"));
    }

    @Test
    void testRegisterUsernameExists() throws Exception {
        // Given
        when(userRepository.findByUsername("johndoe")).thenReturn(new User());

        // When & Then
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("name", "John Doe")
                .param("username", "johndoe")
                .param("password", "password")
                .param("confirmPassword", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("message", "Username already exists"));
    }
}