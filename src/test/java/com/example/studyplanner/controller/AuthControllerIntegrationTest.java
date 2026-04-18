package com.example.studyplanner.controller;

import com.example.studyplanner.dto.request.LoginRequest;
import com.example.studyplanner.dto.request.RegisterRequest;
import com.example.studyplanner.dto.response.AuthResponse;
import com.example.studyplanner.security.JwtAuthenticationFilter;
import com.example.studyplanner.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AuthController Integration Tests")
class AuthControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private AuthService authService;
    
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    private RegisterRequest validRegisterRequest;
    private LoginRequest validLoginRequest;
    
    @BeforeEach
    void setUp() {
        validRegisterRequest = RegisterRequest.builder()
                .fullName("John Doe")
                .email("john@example.com")
                .password("TestPassword123")
                .build();
        
        validLoginRequest = LoginRequest.builder()
                .email("john@example.com")
                .password("TestPassword123")
                .build();
    }
    
    @Test
    @DisplayName("POST /api/auth/register - Successfully register new user")
    void testRegisterSuccess() throws Exception {
        // Arrange
        AuthResponse mockResponse = AuthResponse.builder()
                .token("mock-jwt-token")
                .type("Bearer")
                .message("User registered successfully")
                .build();
        
        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(mockResponse);
        
        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", equalTo("mock-jwt-token")))
                .andExpect(jsonPath("$.type", equalTo("Bearer")))
                .andExpect(jsonPath("$.message", equalTo("User registered successfully")));
    }
    
    @Test
    @DisplayName("POST /api/auth/login - Successfully login with valid credentials")
    void testLoginSuccess() throws Exception {
        // Arrange
        AuthResponse mockResponse = AuthResponse.builder()
                .token("mock-login-token")
                .type("Bearer")
                .message("Login successful")
                .build();
        
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(mockResponse);
        
        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", equalTo("mock-login-token")))
                .andExpect(jsonPath("$.type", equalTo("Bearer")))
                .andExpect(jsonPath("$.message", equalTo("Login successful")));
    }
}


