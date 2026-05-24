package com.auth.repository;

import com.auth.model.RefreshToken;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRefreshTokenRepositoryTest {

    @Test
    void saveAndFindAndRevoke() {
        InMemoryRefreshTokenRepository repo = new InMemoryRefreshTokenRepository();

        RefreshToken rt = new RefreshToken();
        rt.setToken("t1");
        rt.setEmail("u@example.com");
        rt.setExpiresAt(Instant.now().plusSeconds(3600));
        rt.setRevoked(false);

        RefreshToken saved = repo.save(rt);
        assertNotNull(saved.getId());

        var found = repo.findByToken("t1");
        assertTrue(found.isPresent());
        assertFalse(found.get().isRevoked());

        repo.revoke(saved);
        var found2 = repo.findByToken("t1");
        assertTrue(found2.isPresent());
        assertTrue(found2.get().isRevoked());
    }
}
