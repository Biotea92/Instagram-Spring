package com.numble.instagram.application.auth.token;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class RefreshTokenProviderTest {

    @Value("${token.refresh.valid-time-in-days}")
    private long validTimeInDays;

    @Test
    public void testCreateToken() {
        Long userId = 1L;

        RefreshTokenProvider refreshTokenProvider = new RefreshTokenProvider(validTimeInDays);
        RefreshToken refreshToken = refreshTokenProvider.createToken(userId);

        assertNotNull(refreshToken);
        assertEquals(userId, refreshToken.userId());
        assertTrue(refreshToken.expiredAt().isAfter(LocalDateTime.now().minusDays(1)));

        LocalDateTime expectedExpirationTime = LocalDateTime.now().plusDays(validTimeInDays);
        assertEquals(expectedExpirationTime.truncatedTo(ChronoUnit.SECONDS), refreshToken.expiredAt().truncatedTo(ChronoUnit.SECONDS));
    }
}
