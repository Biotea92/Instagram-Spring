package com.numble.instagram.presentation.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RefreshTokenCookieProviderTest {

    private static final String REFRESH_TOKEN = "refreshToken";

    @Autowired
    private RefreshTokenCookieProvider refreshTokenCookieProvider;

    @Test
    @DisplayName("refreshToken의 쿠키를 생성한다.")
    void createCookie() {
        ResponseCookie cookie = refreshTokenCookieProvider.createCookie("testRefreshToken");

        assertEquals(REFRESH_TOKEN, cookie.getName());
        assertEquals("testRefreshToken", cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
        assertEquals("/", cookie.getPath());
        assertEquals(Cookie.SameSite.NONE.attributeValue(), cookie.getSameSite());
        assertEquals(Duration.ofDays(7), cookie.getMaxAge());
    }

    @Test
    @DisplayName("refreshToken의 쿠키를 제거한다.")
    void createLogoutCookie() {
        ResponseCookie cookie = refreshTokenCookieProvider.createLogoutCookie();

        assertEquals(REFRESH_TOKEN, cookie.getName());
        assertEquals("", cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
        assertEquals("/", cookie.getPath());
        assertEquals(Cookie.SameSite.NONE.attributeValue(), cookie.getSameSite());
        assertEquals(Duration.ZERO, cookie.getMaxAge());
    }
}