package com.gfg.userservice.service;

import com.gfg.userservice.domain.dto.UserDTO;
import com.gfg.userservice.domain.entity.User;
import com.gfg.userservice.exceptions.UserObjectNotFoundException;
import com.gfg.userservice.helperClass.UserMapping;
import com.gfg.userservice.repository.UserRepository;
import com.gfg.userservice.service.serviceImpl.UserServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapping userMapping;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldFindAllUsers() {
        // Given
        List<User> users = Arrays.asList(
                User.builder().userId(1).firstName("John").build(),
                User.builder().userId(2).firstName("Jane").build()
        );
        List<UserDTO> userDTOs = Arrays.asList(
                UserDTO.builder().userId(1).firstName("John").build(),
                UserDTO.builder().userId(2).firstName("Jane").build()
        );

        when(userRepository.findAll()).thenReturn(users);
        when(userMapping.mapToDto(users.get(0))).thenReturn(userDTOs.get(0));
        when(userMapping.mapToDto(users.get(1))).thenReturn(userDTOs.get(1));

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
        User user = User.builder().userId(1).firstName("John").build();
        UserDTO userDTO = UserDTO.builder().userId(1).firstName("John").build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMapping.mapToDto(user)).thenReturn(userDTO);

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
        UserDTO userDTO = UserDTO.builder().firstName("John").build();
        User user = User.builder().firstName("John").build();
        User savedUser = User.builder().userId(1).firstName("John").build();
        UserDTO savedUserDTO = UserDTO.builder().userId(1).firstName("John").build();

        when(userMapping.mapToEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapping.mapToDto(savedUser)).thenReturn(savedUserDTO);

        // When
        UserDTO result = userService.save(userDTO);

        // Then
        assertThat(result.getUserId()).isEqualTo(1);
        verify(userRepository).save(user);
    }

    @Test
    void shouldDeleteUser() {
        // Given
        when(userRepository.existsById(1)).thenReturn(true);

        // When
        userService.deleteById(1);

        // Then
        verify(userRepository).deleteById(1);
    }
}