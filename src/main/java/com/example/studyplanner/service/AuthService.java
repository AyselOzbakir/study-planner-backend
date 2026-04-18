package com.example.studyplanner.service;

import com.example.studyplanner.dto.request.LoginRequest;
import com.example.studyplanner.dto.request.RegisterRequest;
import com.example.studyplanner.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
