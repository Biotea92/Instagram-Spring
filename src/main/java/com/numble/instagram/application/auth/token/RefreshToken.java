package com.numble.instagram.application.auth.token;

import java.time.LocalDateTime;
import java.util.UUID;

public record RefreshToken(String refreshToken, Long userId, LocalDateTime expiredAt) {

    public static RefreshToken create(Long userId, long validTimeInDays) {
        return new RefreshToken(UUID.randomUUID().toString(), userId, LocalDateTime.now().plusDays(validTimeInDays));
    }

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }
}
