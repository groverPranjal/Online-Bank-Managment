package com.codeclause.project2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.codeclause.project2.model.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        // Given
        User user = new User("John Doe", "johndoe", "password", "USER");
        userRepository.save(user);

        // When
        User found = userRepository.findByUsername("johndoe");

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("johndoe");
        assertThat(found.getName()).isEqualTo("John Doe");
    }

    @Test
    void testFindByUsernameNotFound() {
        // When
        User found = userRepository.findByUsername("nonexistent");

        // Then
        assertThat(found).isNull();
    }
}