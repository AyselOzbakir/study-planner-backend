package com.example.studyplanner.service.impl;

import com.example.studyplanner.dto.response.UserResponse;
import com.example.studyplanner.entity.User;
import com.example.studyplanner.exception.ResourceNotFoundException;
import com.example.studyplanner.mapper.UserMapper;
import com.example.studyplanner.repository.UserRepository;
import com.example.studyplanner.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserMapper.toResponse(user);
    }
}
