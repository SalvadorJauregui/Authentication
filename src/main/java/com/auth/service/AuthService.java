package com.auth.service;

import com.auth.dto.AuthRequest;
import com.auth.dto.AuthResponse;
import com.auth.dto.RegisterRequest;
import com.auth.dto.UserResponse;
import com.auth.exception.ResourceNotFoundException;
import com.auth.model.Role;
import com.auth.model.User;
import com.auth.repository.UserRepository;
import com.auth.security.JwtProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    private static final Logger LOGGER = LogManager.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       JwtProvider jwtProvider,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // In a real implementation, password hashing would occur here.
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(encodedPassword);
        newUser.setRoles(Collections.singletonList(Role.USER));

        User savedUser = userRepository.save(newUser);
        LOGGER.info("Registered new user with email {}", savedUser.getEmail());

        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRoles());
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid login credentials"));

        // Password verification placeholder. Replace with BCrypt or Argon2 in production.
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid login credentials");
        }

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        LOGGER.info("User {} authenticated successfully", user.getEmail());
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh token is invalid");
        }

        String email = jwtProvider.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User associated with refresh token not found"));

        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);

        LOGGER.info("Refreshed tokens for user {}", user.getEmail());
        return new AuthResponse(newAccessToken, newRefreshToken);
    }
}
