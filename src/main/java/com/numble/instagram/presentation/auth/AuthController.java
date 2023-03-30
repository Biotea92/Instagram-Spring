package com.numble.instagram.presentation.auth;

import com.numble.instagram.application.auth.AuthService;
import com.numble.instagram.dto.common.LoginDto;
import com.numble.instagram.dto.request.auth.LoginRequest;
import com.numble.instagram.dto.response.auth.LoginResponse;
import com.numble.instagram.exception.unauthorized.RefreshTokenNotExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.numble.instagram.presentation.auth.RefreshTokenCookieProvider.REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginDto loginDto = authService.login(loginRequest.nickname(), loginRequest.password());
        ResponseCookie cookie = refreshTokenCookieProvider.createCookie(loginDto.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(LoginResponse.from(loginDto.accessToken()));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = REFRESH_TOKEN, required = false) String refreshToken) {
        validateRefreshTokenExists(refreshToken);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookieProvider.createLogoutCookie().toString())
                .build();
    }

    private void validateRefreshTokenExists(final String refreshToken) {
        if (refreshToken == null) {
            throw new RefreshTokenNotExistsException();
        }
    }
}
