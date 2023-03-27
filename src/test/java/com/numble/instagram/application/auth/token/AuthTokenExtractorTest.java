package com.numble.instagram.application.auth.token;

import com.numble.instagram.exception.unauthorized.TokenMalformedException;
import com.numble.instagram.exception.unauthorized.TokenNotExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthTokenExtractorTest {

    private AuthTokenExtractor authTokenExtractor;

    @BeforeEach
    void setUp() {
        authTokenExtractor = new AuthTokenExtractor();
    }

    @Test
    @DisplayName("정상 토큰이 추출되어야한다.")
    void extractTokenWithValidAuthorizationHeader() {
        String token = "valid-token";
        String authorizationHeader = "Bearer " + token;
        assertEquals(token, authTokenExtractor.extractToken(authorizationHeader));
    }

    @Test
    @DisplayName("토큰이 없으면 예외가 발생된다.")
    public void extractTokenWithNullAuthorizationHeader() {
        assertThrows(TokenNotExistsException.class, () -> authTokenExtractor.extractToken(null));
    }

    @Test
    @DisplayName("토큰 형식이 맞지 않으면 예외가 발생한다.")
    public void extractTokenWithMalformedAuthorizationHeader() {
        assertThrows(TokenMalformedException.class, () -> authTokenExtractor.extractToken("invalid-token-format"));
        assertThrows(TokenMalformedException.class, () -> authTokenExtractor.extractToken("Bearer"));
        assertThrows(TokenMalformedException.class, () -> authTokenExtractor.extractToken("Token abcdefg"));
    }
}