package com.auth.service;

import com.auth.dto.UserResponse;
import com.auth.exception.ResourceNotFoundException;
import com.auth.model.User;
import com.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for email: " + email));
    }

    public UserResponse getProfile(String email) {
        User user = findByEmail(email);
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + id));
    }
}
