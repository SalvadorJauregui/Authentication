package com.auth.security;

import com.auth.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private static final Logger LOGGER = LogManager.getLogger(JwtProvider.class);
    private static final String SECRET_KEY = "change-this-secret-for-demo-only";
    private static final long ACCESS_TOKEN_EXPIRY_MINUTES = 15;
    private static final long REFRESH_TOKEN_EXPIRY_DAYS = 30;

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(ACCESS_TOKEN_EXPIRY_MINUTES, ChronoUnit.MINUTES);

        // JWT creation logic would normally include claims and signing.
        String token = String.format("access-%s-%s", user.getEmail(), UUID.randomUUID());
        LOGGER.debug("Generated access token for user {} expiring at {}", user.getEmail(), Date.from(expiry));
        return token;
    }

    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(REFRESH_TOKEN_EXPIRY_DAYS, ChronoUnit.DAYS);

        // Refresh token generation logic would normally be cryptographically signed.
        String token = String.format("refresh-%s-%s", user.getEmail(), UUID.randomUUID());
        LOGGER.debug("Generated refresh token for user {} expiring at {}", user.getEmail(), Date.from(expiry));
        return token;
    }

    public boolean validateToken(String token) {
        // Placeholder validation. In a real implementation, parse and verify the JWT.
        boolean valid = token != null && (token.startsWith("access-") || token.startsWith("refresh-"));
        if (!valid) {
            LOGGER.warn("Invalid token detected: {}", token);
        }
        return valid;
    }

    public String getEmailFromToken(String token) {
        // Placeholder extraction. A real JWT provider would parse the token payload.
        if (token == null || !token.contains("-")) {
            return null;
        }
        String[] parts = token.split("-");
        return parts.length >= 2 ? parts[1] : null;
    }
}
