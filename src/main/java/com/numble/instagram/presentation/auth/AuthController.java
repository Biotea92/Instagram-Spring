package com.numble.instagram.presentation.auth;

import com.numble.instagram.application.auth.AuthService;
import com.numble.instagram.dto.request.auth.LoginRequest;
import com.numble.instagram.dto.common.LoginDto;
import com.numble.instagram.dto.response.auth.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
