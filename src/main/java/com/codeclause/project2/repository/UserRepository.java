package com.codeclause.project2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeclause.project2.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}