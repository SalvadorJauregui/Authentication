package com.auth.repository;

import com.auth.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken token);

    Optional<RefreshToken> findByToken(String token);

    void revoke(RefreshToken token);
}
