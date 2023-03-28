package com.numble.instagram.application.auth.token;

import java.time.LocalDateTime;

public record RefreshToken(String refreshToken, Long userId, LocalDateTime expiredAt) {

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }
}
