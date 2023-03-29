package com.numble.instagram.util.fixture.auth;

import com.numble.instagram.application.auth.token.RefreshToken;

import java.time.LocalDateTime;

public class RefreshTokenFixture {

    public static RefreshToken create(String tokenValue, Long userId, LocalDateTime expiredAt) {
        return new RefreshToken(tokenValue, userId, expiredAt);
    }
}
