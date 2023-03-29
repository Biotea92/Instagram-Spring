package com.numble.instagram.presentation.auth;

import com.numble.instagram.application.auth.token.TokenProvider;
import com.numble.instagram.exception.unauthorized.TokenExpiredException;
import com.numble.instagram.exception.unauthorized.TokenNotExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.method.HandlerMethod;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HandlerMethod handlerMethod;

    private AuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new AuthenticationInterceptor(tokenProvider);
    }

    @Test
    @DisplayName("preHandle method는 Login Annotation이 없으면 true를 반환한다.")
    void testPreHandle_shouldReturnTrueWhenNoLoginAnnotationPresent() {
        boolean result = interceptor.preHandle(request, response, mock(HandlerMethod.class));
        assertTrue(result);
    }

    @Test
    @DisplayName("Authorization이 valid-token이면 true를 반환한다.")
    void testPreHandle_shouldReturnTrueWhenAuthorizationHeaderPresentAndValid() {
        when(handlerMethod.getMethodAnnotation(Login.class)).thenReturn(mock(Login.class));
        when(request.getHeader("Authorization")).thenReturn("valid-token");
        when(tokenProvider.isValidToken("valid-token")).thenReturn(true);

        boolean result = interceptor.preHandle(request, response, handlerMethod);
        assertTrue(result);

        verify(tokenProvider, times(1)).isValidToken("valid-token");
        verifyNoInteractions(response);
    }

    @Test
    @DisplayName("Authorization이 invalid-token이면 TokenExpiredException가 발생한다.")
    void testPreHandle_shouldThrowTokenExpiredExceptionWhenAuthorizationHeaderPresentAndInvalid() {
        when(handlerMethod.getMethodAnnotation(Login.class)).thenReturn(mock(Login.class));
        when(request.getHeader("Authorization")).thenReturn("invalid-token");
        when(tokenProvider.isValidToken("invalid-token")).thenReturn(false);

        assertThrows(TokenExpiredException.class, () -> interceptor.preHandle(request, response, handlerMethod));

        verify(tokenProvider, times(1)).isValidToken("invalid-token");
        verifyNoInteractions(response);
    }

    @Test
    @DisplayName("Authorization이 없고 Login Annotation이 있으면 예외가 발생한다.")
    void testPreHandle_shouldThrowTokenNotExistsExceptionWhenLoginAnnotationRequired() {
        Login login = mock(Login.class);
        when(login.required()).thenReturn(true);
        when(handlerMethod.getMethodAnnotation(Login.class)).thenReturn(login);

        assertThrows(TokenNotExistsException.class, () -> interceptor.preHandle(request, response, handlerMethod));

        verifyNoInteractions(tokenProvider, response);
    }
}