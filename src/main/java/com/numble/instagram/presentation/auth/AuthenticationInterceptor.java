package com.numble.instagram.presentation.auth;

import com.numble.instagram.application.auth.token.TokenProvider;
import com.numble.instagram.exception.unauthorized.TokenExpiredException;
import com.numble.instagram.exception.unauthorized.TokenNotExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Login auth = getLoginAnnotation(handler);
        if (!(handler instanceof HandlerMethod) || auth == null) {
            return true;
        }

        if (request.getHeader(AUTHORIZATION) != null) {
            validateToken(request);
            return true;
        }

        validateTokenRequired(auth);
        return true;
    }

    private void validateTokenRequired(Login auth) {
        if (auth.required()) {
            throw new TokenNotExistsException();
        }
    }

    private void validateToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (!tokenProvider.isValidToken(authorizationHeader)) {
            throw new TokenExpiredException();
        }
    }

    private Login getLoginAnnotation(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        return handlerMethod.getMethodAnnotation(Login.class);
    }
}
