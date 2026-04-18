package com.example.studyplanner.controller;

import com.example.studyplanner.dto.response.UserResponse;
import com.example.studyplanner.enums.Role;
import com.example.studyplanner.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.example.studyplanner.security.CustomUserDetailsService;
import com.example.studyplanner.security.JwtService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AdminController Integration Tests")
class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private UserResponse adminUser;
    private UserResponse regularUser;

    @BeforeEach
    void setUp() {
        adminUser = UserResponse.builder()
                .id(1L)
                .fullName("Admin User")
                .email("admin@example.com")
                .role(Role.ADMIN)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        regularUser = UserResponse.builder()
                .id(2L)
                .fullName("Regular User")
                .email("user@example.com")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("GET /api/admin/users - Returns 200 with all users as JSON")
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void testGetAllUsers() throws Exception {
        // Arrange
        List<UserResponse> users = Arrays.asList(adminUser, regularUser);
        when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].fullName", equalTo("Admin User")))
                .andExpect(jsonPath("$[0].email", equalTo("admin@example.com")))
                .andExpect(jsonPath("$[0].role", equalTo("ADMIN")))
                .andExpect(jsonPath("$[1].id", equalTo(2)))
                .andExpect(jsonPath("$[1].fullName", equalTo("Regular User")))
                .andExpect(jsonPath("$[1].role", equalTo("USER")));
    }
}
