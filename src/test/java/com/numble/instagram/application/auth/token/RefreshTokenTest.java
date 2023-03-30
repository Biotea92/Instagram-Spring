package com.numble.instagram.application.auth.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RefreshTokenTest {

    @Test
    @DisplayName("리프레시토큰의 만료시간이 현재 시간보다 전이면 true를 반환한다.")
    void isExpired_ReturnsTrue_WhenExpiredAtIsBeforeCurrentTime() {
        LocalDateTime expiredAt = LocalDateTime.now().minusMinutes(1);
        RefreshToken refreshToken = new RefreshToken("token", 1L, expiredAt);

        boolean result = refreshToken.isExpired();

        assertTrue(result);
    }

    @Test
    @DisplayName("리프레시토큰의 만료시간이 현재 시간보다 후면 false를 반환한다.")
    void isExpired_ReturnsFalse_WhenExpiredAtIsAfterCurrentTime() {
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(1);
        RefreshToken refreshToken = new RefreshToken("token", 1L, expiredAt);

        boolean result = refreshToken.isExpired();

        assertFalse(result);
    }
}