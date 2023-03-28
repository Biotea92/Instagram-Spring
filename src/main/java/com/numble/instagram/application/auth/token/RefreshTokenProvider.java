package com.numble.instagram.application.auth.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RefreshTokenProvider {

    private final long validTimeInDays;

    public RefreshTokenProvider(@Value("${token.refresh.valid-time-in-days}") long validTimeInDays) {
        this.validTimeInDays = validTimeInDays;
    }

    public RefreshToken createToken(Long userId) {
        return new RefreshToken(UUID.randomUUID().toString(), userId, LocalDateTime.now().plusDays(validTimeInDays));
    }
}
