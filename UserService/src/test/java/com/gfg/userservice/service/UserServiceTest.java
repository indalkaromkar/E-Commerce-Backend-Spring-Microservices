package com.gfg.userservice.service;

import com.gfg.userservice.domain.dto.CredentialDTO;
import com.gfg.userservice.domain.dto.UserDTO;
import com.gfg.userservice.domain.entity.Credential;
import com.gfg.userservice.domain.entity.User;
import com.gfg.userservice.domain.enums.RoleBasedAuthority;
import com.gfg.userservice.exceptions.UserObjectNotFoundException;
import com.gfg.userservice.repository.CredentialRepository;
import com.gfg.userservice.repository.UserRepository;
import com.gfg.userservice.security.JwtUtil;
import com.gfg.userservice.service.serviceImpl.UserServiceImpl;
import com.gfg.userservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldFindAllUsers() {
        // Given
        Credential credential1 = Credential.builder()
                .credentialId(1)
                .username("john")
                .password("password")
                .roleBasedAuthority(RoleBasedAuthority.ROLE_USER)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .build();

        Credential credential2 = Credential.builder()
                .credentialId(2)
                .username("jane")
                .password("password")
                .roleBasedAuthority(RoleBasedAuthority.ROLE_USER)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .build();

        List<User> users = Arrays.asList(
                User.builder().userId(1).firstName("John").credential(credential1).build(),
                User.builder().userId(2).firstName("Jane").credential(credential2).build());

        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserDTO> result = userService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        verify(userRepository).findAll();
    }

    @Test
    void shouldFindUserById() {
        // Given
        Credential credential = Credential.builder()
                .credentialId(1)
                .username("john")
                .password("password")
                .roleBasedAuthority(RoleBasedAuthority.ROLE_USER)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .build();

        User user = User.builder().userId(1).firstName("John").credential(credential).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // When
        UserDTO result = userService.findById(1);

        // Then
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(userRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.findById(1))
                .isInstanceOf(UserObjectNotFoundException.class);
    }

    @Test
    void shouldSaveUser() {
        // Given
        CredentialDTO credentialDTO = CredentialDTO.builder()
                .username("john")
                .password("password")
                .roleBasedAuthority(RoleBasedAuthority.ROLE_USER)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialNonExpired(true)
                .build();

        UserDTO userDTO = UserDTO.builder().firstName("John").credentialDTO(credentialDTO).build();

        Credential savedCredential = Credential.builder()
                .credentialId(1)
                .username("john")
                .password("password")
                .roleBasedAuthority(RoleBasedAuthority.ROLE_USER)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .build();

        User savedUser = User.builder().userId(1).firstName("John").credential(savedCredential).build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        UserDTO result = userService.save(userDTO);

        // Then
        assertThat(result.getUserId()).isEqualTo(1);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldDeleteUser() {
        // When
        userService.deleteById(1);

        // Then
        verify(userRepository).deleteById(1);
    }
}