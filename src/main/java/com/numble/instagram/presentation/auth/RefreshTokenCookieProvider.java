package com.numble.instagram.presentation.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RefreshTokenCookieProvider {

    protected static final String REFRESH_TOKEN = "refreshToken";
    private static final int REMOVAL_MAX_AGE = 0;

    private final long validTimeInDays;

    public RefreshTokenCookieProvider(@Value("${token.refresh.valid-time-in-days}") long validTimeInDays) {
        this.validTimeInDays = validTimeInDays;
    }

    public ResponseCookie createCookie(final String refreshToken) {
        return createTokenCookieBuilder(refreshToken)
                .maxAge(Duration.ofDays(validTimeInDays))
                .build();
    }

    public ResponseCookie createLogoutCookie() {
        return createTokenCookieBuilder("")
                .maxAge(REMOVAL_MAX_AGE)
                .build();
    }

    private ResponseCookieBuilder createTokenCookieBuilder(final String value) {
        return ResponseCookie.from(REFRESH_TOKEN, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(SameSite.NONE.attributeValue());
    }
}
