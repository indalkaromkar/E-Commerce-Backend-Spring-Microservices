package com.gfg.userservice.repository;

import com.gfg.userservice.domain.entity.Credential;
import com.gfg.userservice.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByCredentialUsername() {
        // Given
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();
        
        Credential credential = Credential.builder()
                .username("johndoe")
                .password("password")
                .user(user)
                .build();
        
        user.setCredential(credential);
        entityManager.persistAndFlush(user);

        // When
        Optional<User> found = userRepository.findByCredentialUsername("johndoe");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("John");
        assertThat(found.get().getCredential().getUsername()).isEqualTo("johndoe");
    }

    @Test
    void shouldReturnEmptyWhenUsernameNotFound() {
        // When
        Optional<User> found = userRepository.findByCredentialUsername("nonexistent");

        // Then
        assertThat(found).isEmpty();
    }
}