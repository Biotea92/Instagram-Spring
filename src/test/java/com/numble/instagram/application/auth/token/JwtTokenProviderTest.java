package com.numble.instagram.application.auth.token;

import com.numble.instagram.exception.unauthorized.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private static final String secretKey = "TestLJNO9KUPS2RN+HVVUYT9LUDMUAGIMUEMKVXR4DH5I=";
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
            new AuthTokenExtractor(), secretKey, 7200000L
    );

    @Test
    @DisplayName("토큰이 생성된다.")
    void createToken() {
        Long userId = 123L;
        String token = jwtTokenProvider.createAccessToken(userId);

        assertNotNull(token);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token);

        assertEquals(ACCESS_TOKEN_SUBJECT, claims.getBody().getSubject());
        assertEquals(userId, claims.getBody().get("id", Long.class));
    }

    @Test
    @DisplayName("유효한 토큰이면 True를 반환한다.")
    void isValidTokenShouldReturnTrueForValidToken() {
        Long userId = 123L;
        String token = jwtTokenProvider.createAccessToken(userId);

        boolean isValid = jwtTokenProvider.isValidToken("Bearer " + token);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("userId로 토큰을 만들면 payload에서 같은 userId를 반환해야한다.")
    void getUserIdShouldReturnValidUserIdForValidToken() {
        Long userId = 123L;
        String token = jwtTokenProvider.createAccessToken(userId);

        Long resultUserId = jwtTokenProvider.getUserId("Bearer " + token);

        assertEquals(userId, resultUserId);
    }

    @Test
    @DisplayName("유효기간이 지난 토큰은 TokenExpiredException이 발생한다.")
    void getUserIdShouldThrowTokenExpiredExceptionForExpiredToken() {
        Long userId = 123L;
        jwtTokenProvider = new JwtTokenProvider(new AuthTokenExtractor(), secretKey, -1000L);
        String token = jwtTokenProvider.createAccessToken(userId);

        assertThrows(TokenExpiredException.class, () -> jwtTokenProvider.getUserId("Bearer " + token));
    }

    @Test
    @DisplayName("유효하지 않은 토큰은 false를 반환한다.")
    void isValidTokenShouldReturnFalseForInvalidToken() {
        String token = "invalid-token";

        boolean isValid = jwtTokenProvider.isValidToken("Bearer " + token);

        assertFalse(isValid);
    }
}