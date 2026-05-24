package com.auth.model;

import java.time.Instant;
import java.util.UUID;

public class RefreshToken {

    private UUID id;
    private String token;
    private String email;
    private Instant expiresAt;
    private boolean revoked;

    public RefreshToken() {
    }

    public RefreshToken(UUID id, String token, String email, Instant expiresAt, boolean revoked) {
        this.id = id;
        this.token = token;
        this.email = email;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
