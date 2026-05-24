package com.auth.repository;

import com.auth.model.RefreshToken;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRefreshTokenRepository implements RefreshTokenRepository {

    private final Map<String, RefreshToken> tokens = new ConcurrentHashMap<>();

    @Override
    public RefreshToken save(RefreshToken token) {
        if (token.getId() == null) {
            token.setId(UUID.randomUUID());
        }
        tokens.put(token.getToken(), token);
        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return Optional.ofNullable(tokens.get(token));
    }

    @Override
    public void revoke(RefreshToken token) {
        RefreshToken existing = tokens.get(token.getToken());
        if (existing != null) {
            existing.setRevoked(true);
            tokens.put(existing.getToken(), existing);
        }
    }
}
