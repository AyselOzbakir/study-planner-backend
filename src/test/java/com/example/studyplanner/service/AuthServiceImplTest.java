package com.example.studyplanner.service;

import com.example.studyplanner.dto.request.LoginRequest;
import com.example.studyplanner.dto.request.RegisterRequest;
import com.example.studyplanner.dto.response.AuthResponse;
import com.example.studyplanner.entity.User;
import com.example.studyplanner.enums.Role;
import com.example.studyplanner.exception.BadRequestException;
import com.example.studyplanner.exception.ResourceNotFoundException;
import com.example.studyplanner.repository.UserRepository;
import com.example.studyplanner.security.JwtService;
import com.example.studyplanner.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServiceImpl Unit Tests")
class AuthServiceImplTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtService jwtService;
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @InjectMocks
    private AuthServiceImpl authService;
    
    private RegisterRequest validRegisterRequest;
    private LoginRequest validLoginRequest;
    private User testUser;
    
    @BeforeEach
    void setUp() {
        validRegisterRequest = RegisterRequest.builder()
                .fullName("John Doe")
                .email("john@example.com")
                .password("password123")
                .build();
        
        validLoginRequest = LoginRequest.builder()
                .email("john@example.com")
                .password("password123")
                .build();
        
        testUser = User.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .password("encoded_password")
                .role(Role.USER)
                .build();
    }
    
    @Test
    @DisplayName("Successfully register new user")
    void testRegisterSuccess() {
        // Arrange
        when(userRepository.existsByEmail(validRegisterRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(validRegisterRequest.getPassword())).thenReturn("encoded_password");
        when(jwtService.generateToken(validRegisterRequest.getEmail())).thenReturn("test_token");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // Act
        AuthResponse response = authService.register(validRegisterRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals("test_token", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals("User registered successfully", response.getMessage());
        
        verify(userRepository, times(1)).existsByEmail(validRegisterRequest.getEmail());
        verify(passwordEncoder, times(1)).encode(validRegisterRequest.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    @DisplayName("Register fails when email already exists")
    void testRegisterFailsEmailExists() {
        // Arrange
        when(userRepository.existsByEmail(validRegisterRequest.getEmail())).thenReturn(true);
        
        // Act & Assert
        assertThrows(BadRequestException.class, () -> authService.register(validRegisterRequest),
                "Should throw BadRequestException for existing email");
        
        verify(userRepository, times(1)).existsByEmail(validRegisterRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    @DisplayName("Successfully login existing user")
    void testLoginSuccess() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(testUser.getEmail(), testUser.getPassword()));
        when(userRepository.findByEmail(validLoginRequest.getEmail())).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(testUser.getEmail())).thenReturn("test_token");
        
        // Act
        AuthResponse response = authService.login(validLoginRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals("test_token", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals("Login successful", response.getMessage());
        
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(validLoginRequest.getEmail());
        verify(jwtService, times(1)).generateToken(testUser.getEmail());
    }
    
    @Test
    @DisplayName("Login fails when user not found")
    void testLoginFailsUserNotFound() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(validLoginRequest.getEmail(), validLoginRequest.getPassword()));
        when(userRepository.findByEmail(validLoginRequest.getEmail())).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> authService.login(validLoginRequest),
                "Should throw ResourceNotFoundException when user not found");
        
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(validLoginRequest.getEmail());
        verify(jwtService, never()).generateToken(anyString());
    }
    
    @Test
    @DisplayName("Registered user has USER role by default")
    void testRegisterAssignsUserRole() {
        // Arrange
        when(userRepository.existsByEmail(validRegisterRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(validRegisterRequest.getPassword())).thenReturn("encoded_password");
        when(jwtService.generateToken(validRegisterRequest.getEmail())).thenReturn("test_token");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertNotNull(savedUser.getRole());
            assertEquals(Role.USER, savedUser.getRole());
            return testUser;
        });
        
        // Act
        authService.register(validRegisterRequest);
        
        // Assert
        verify(userRepository).save(argThat(user -> Role.USER.equals(user.getRole())));
    }
}
