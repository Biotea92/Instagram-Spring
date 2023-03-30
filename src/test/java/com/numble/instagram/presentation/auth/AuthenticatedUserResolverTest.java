package com.numble.instagram.presentation.auth;

import com.numble.instagram.application.auth.token.TokenProvider;
import com.numble.instagram.exception.unauthorized.TokenExpiredException;
import com.numble.instagram.exception.unauthorized.TokenNotExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.NativeWebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticatedUserResolverTest {

    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private MethodParameter parameter;
    @Mock
    private NativeWebRequest webRequest;
    private AuthenticatedUserResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new AuthenticatedUserResolver(tokenProvider);
    }

    @Test
    @DisplayName("AuthenticatedMember 파라미터 어노테이션이 있으면 true를 반환한다.")
    void testSupportsParameter_shouldReturnTrueWhenParameterHasAuthenticatedMemberAnnotation() {
        when(parameter.hasParameterAnnotation(AuthenticatedUser.class)).thenReturn(true);

        boolean result = resolver.supportsParameter(parameter);

        assertTrue(result);
    }

    @Test
    @DisplayName(("AuthenticatedMember 파라미터 어노테이션이 없으면 false를 반환한다."))
    void testSupportsParameter_shouldReturnFalseWhenParameterDoesNotHaveAuthenticatedMemberAnnotation() {
        when(parameter.hasParameterAnnotation(AuthenticatedUser.class)).thenReturn(false);

        boolean result = resolver.supportsParameter(parameter);

        assertFalse(result);
    }

    @Test
    @DisplayName("Authorization 헤더에 valid-token이 존재하면 userId를 반환한다.")
    void testResolveArgument_shouldReturnUserIdWhenAuthorizationHeaderPresentAndValid() {
        String authorizationHeader = "valid-token";
        Long userId = 123L;

        when(webRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationHeader);
        when(tokenProvider.isValidToken(authorizationHeader)).thenReturn(true);
        when(tokenProvider.getUserId(authorizationHeader)).thenReturn(userId);

        Object result = resolver.resolveArgument(null, null, webRequest, null);

        assertEquals(userId, result);

        verify(tokenProvider, times(1)).isValidToken(authorizationHeader);
        verify(tokenProvider, times(1)).getUserId(authorizationHeader);
        verifyNoMoreInteractions(tokenProvider);
    }

    @Test
    @DisplayName("Authorization 헤더에 아무것도 없으면 TokenNotExistsException이 발생한다.")
    void testResolveArgument_shouldThrowTokenNotExistsExceptionWhenAuthorizationHeaderNotPresent() {
        when(webRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        assertThrows(TokenNotExistsException.class,
                () -> resolver.resolveArgument(null, null, webRequest, null));

        verifyNoInteractions(tokenProvider);
    }

    @Test
    void testResolveArgument_shouldThrowTokenExpiredExceptionWhenTokenIsExpired() {
        String authorizationHeader = "expired-token";

        when(webRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationHeader);
        when(tokenProvider.isValidToken(authorizationHeader)).thenReturn(false);

        assertThrows(TokenExpiredException.class,
                () -> resolver.resolveArgument(null, null, webRequest, null));

        verify(tokenProvider, times(1)).isValidToken(authorizationHeader);
        verifyNoMoreInteractions(tokenProvider);
    }
}