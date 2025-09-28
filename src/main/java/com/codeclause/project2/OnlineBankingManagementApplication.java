package com.codeclause.project2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.codeclause.project2.model.User;
import com.codeclause.project2.repository.UserRepository;

@SpringBootApplication
public class OnlineBankingManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineBankingManagementApplication.class, args);
	}

	@Bean
	public CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByUsername("admin") == null) {
				User admin = new User("Admin", "admin", passwordEncoder.encode("admin123"), "ADMIN");
				userRepository.save(admin);
			}
		};
	}

}
