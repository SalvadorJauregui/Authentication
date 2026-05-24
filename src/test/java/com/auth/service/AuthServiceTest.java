package com.auth.service;

import com.auth.dto.AuthRequest;
import com.auth.dto.AuthResponse;
import com.auth.model.RefreshToken;
import com.auth.model.Role;
import com.auth.model.User;
import com.auth.repository.RefreshTokenRepository;
import com.auth.repository.UserRepository;
import com.auth.security.JwtProvider;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Test
    void login_persistsRefreshToken() {
        UserRepository userRepository = mock(UserRepository.class);
        JwtProvider jwtProvider = mock(JwtProvider.class);
        RefreshTokenRepository refreshTokenRepository = mock(RefreshTokenRepository.class);
        org.springframework.security.crypto.password.PasswordEncoder passwordEncoder = mock(org.springframework.security.crypto.password.PasswordEncoder.class);

        User user = new User(UUID.randomUUID(), "jdoe", "jdoe@example.com", "encoded", List.of(Role.USER));

        when(userRepository.findByEmail("jdoe@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plain", "encoded")).thenReturn(true);
        when(jwtProvider.generateAccessToken(user)).thenReturn("access-token");
        when(jwtProvider.generateRefreshToken(user)).thenReturn("refresh-token");

        AuthService authService = new AuthService(userRepository, jwtProvider, passwordEncoder, refreshTokenRepository);

        AuthRequest request = new AuthRequest();
        request.setEmail("jdoe@example.com");
        request.setPassword("plain");

        AuthResponse response = authService.login(request);

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    void refresh_revokesOldAndSavesNew() {
        UserRepository userRepository = mock(UserRepository.class);
        JwtProvider jwtProvider = mock(JwtProvider.class);
        RefreshTokenRepository refreshTokenRepository = mock(RefreshTokenRepository.class);
        org.springframework.security.crypto.password.PasswordEncoder passwordEncoder = mock(org.springframework.security.crypto.password.PasswordEncoder.class);

        String oldToken = "refresh-old";
        String email = "jdoe@example.com";

        com.auth.model.RefreshToken stored = new com.auth.model.RefreshToken();
        stored.setId(UUID.randomUUID());
        stored.setToken(oldToken);
        stored.setEmail(email);
        stored.setExpiresAt(Instant.now().plusSeconds(3600));
        stored.setRevoked(false);

        User user = new User(UUID.randomUUID(), "jdoe", email, "encoded", List.of(Role.USER));

        when(jwtProvider.validateToken(oldToken)).thenReturn(true);
        when(refreshTokenRepository.findByToken(oldToken)).thenReturn(Optional.of(stored));
        when(jwtProvider.getEmailFromToken(oldToken)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtProvider.generateAccessToken(user)).thenReturn("new-access");
        when(jwtProvider.generateRefreshToken(user)).thenReturn("new-refresh");

        AuthService authService = new AuthService(userRepository, jwtProvider, passwordEncoder, refreshTokenRepository);

        AuthResponse resp = authService.refreshToken(oldToken);

        assertEquals("new-access", resp.getAccessToken());
        assertEquals("new-refresh", resp.getRefreshToken());
        verify(refreshTokenRepository, times(1)).revoke(stored);
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }
}
