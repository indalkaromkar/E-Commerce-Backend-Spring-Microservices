package com.gfg.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfg.userservice.domain.dto.UserDTO;
import com.gfg.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllUsers() throws Exception {
        // Given
        List<UserDTO> users = Arrays.asList(
                UserDTO.builder().userId(1).firstName("John").build(),
                UserDTO.builder().userId(2).firstName("Jane").build()
        );
        when(userService.findAll()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.collection").isArray())
                .andExpect(jsonPath("$.collection[0].firstName").value("John"));
    }

    @Test
    void shouldGetUserById() throws Exception {
        // Given
        UserDTO user = UserDTO.builder().userId(1).firstName("John").build();
        when(userService.findById(1)).thenReturn(user);

        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpected(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldCreateUser() throws Exception {
        // Given
        UserDTO userDTO = UserDTO.builder().firstName("John").email("john@example.com").build();
        UserDTO savedUser = UserDTO.builder().userId(1).firstName("John").email("john@example.com").build();
        when(userService.save(any(UserDTO.class))).thenReturn(savedUser);

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        // Given
        UserDTO userDTO = UserDTO.builder().userId(1).firstName("John Updated").build();
        when(userService.update(eq(1), any(UserDTO.class))).thenReturn(userDTO);

        // When & Then
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John Updated"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}