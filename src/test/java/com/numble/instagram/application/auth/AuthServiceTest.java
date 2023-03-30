package com.numble.instagram.application.auth;

import com.numble.instagram.application.auth.token.RefreshToken;
import com.numble.instagram.application.auth.token.RefreshTokenProvider;
import com.numble.instagram.application.auth.token.RefreshTokenRepository;
import com.numble.instagram.application.auth.token.TokenProvider;
import com.numble.instagram.domain.user.User;
import com.numble.instagram.domain.user.repository.UserRepository;
import com.numble.instagram.dto.common.LoginDto;
import com.numble.instagram.exception.notfound.UserNotFoundException;
import com.numble.instagram.exception.unauthorized.PasswordMismatchException;
import com.numble.instagram.exception.unauthorized.RefreshTokenExpiredException;
import com.numble.instagram.exception.unauthorized.RefreshTokenNotExistsException;
import com.numble.instagram.util.fixture.auth.RefreshTokenFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private RefreshTokenProvider refreshTokenProvider;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("로그인이 성공해야한다.")
    void loginWithCorrectCredentials() {
        String nickname = "test_user";
        String password = "password123";
        Long userId = 1L;
        String refreshTokenValue = "refresh_token_value";
        String accessToken = "access_token";
        RefreshToken refreshToken = RefreshTokenFixture.create(refreshTokenValue, userId, LocalDateTime.now().plusDays(7));
        User user = UserFixture.create(userId, nickname, password);

        when(userRepository.findByNickname(nickname)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(tokenProvider.createAccessToken(userId)).thenReturn(accessToken);
        when(refreshTokenProvider.createToken(userId)).thenReturn(refreshToken);

        LoginDto result = authService.login(nickname, password);

        assertNotNull(result);
        assertEquals(accessToken, result.accessToken());
        assertEquals(refreshToken.tokenValue(), result.refreshToken());
        verify(refreshTokenRepository, times(1)).save(refreshToken);
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인을 하면 PasswordMismatchException이 발생한다.")
    void loginWithIncorrectPasswordThrowsPasswordMismatchException() {
        String nickname = "test_user";
        String password = "password123";
        long userId = 1L;
        User user = UserFixture.create(userId, nickname, password);
        when(userRepository.findByNickname(nickname)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        assertThrows(PasswordMismatchException.class, () -> authService.login(nickname, password));
    }

    @Test
    @DisplayName("존재하지 않은 User로 로그인을 시도하면 UserNotFoundException이 발생한다.")
    void loginWithNonexistentUserThrowsUserNotFoundException() {
        String nickname = "test_user";
        String password = "password123";
        when(userRepository.findByNickname(nickname)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.login(nickname, password));
    }

    @Test
    @DisplayName("토큰이 재발급 되어야한다.")
    void testReissueToken() {
        Long userId = 1L;
        String refreshTokenValue = "valid-refresh-token-value";
        RefreshToken refreshToken = RefreshTokenFixture
                .create(refreshTokenValue, userId, LocalDateTime.now().plusDays(1));

        when(refreshTokenRepository.findTokenByTokenValue(refreshTokenValue)).thenReturn(Optional.of(refreshToken));
        String newAccessToken = "new-access-token";
        RefreshToken newRefreshToken = RefreshTokenFixture
                .create("new-refresh-token-value", userId, LocalDateTime.now().plusDays(7));
        when(tokenProvider.createAccessToken(userId)).thenReturn(newAccessToken);
        when(refreshTokenProvider.createToken(userId)).thenReturn(newRefreshToken);

        LoginDto loginDto = authService.reissueToken(refreshTokenValue);

        assertNotNull(loginDto);
        assertEquals(newAccessToken, loginDto.accessToken());
        assertEquals(newRefreshToken.tokenValue(), loginDto.refreshToken());
        verify(refreshTokenRepository, times(1)).delete(refreshTokenValue);
        verify(refreshTokenRepository, times(1)).save(newRefreshToken);
    }

    @Test
    @DisplayName("DB에 없는 토큰이면 RefreshTokenNotExistsException이 발생한다.")
    public void reissueToken_invalidToken() {
        String refreshTokenValue = "invalid-refresh-token-value";
        when(refreshTokenRepository.findTokenByTokenValue(refreshTokenValue)).thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotExistsException.class, () -> authService.reissueToken(refreshTokenValue));

        verify(refreshTokenRepository, times(1)).findTokenByTokenValue(refreshTokenValue);
        verify(refreshTokenRepository, never()).delete(anyString());
        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    @DisplayName("만료된 토큰이면 발생한다.")
    public void reissueToken_expiredToken() {
        Long userId = 1L;
        String refreshTokenValue = "expired-refresh-token-value";
        RefreshToken expiredRefreshToken = RefreshTokenFixture
                .create(refreshTokenValue, userId, LocalDateTime.now().minusDays(1));
        when(refreshTokenRepository.findTokenByTokenValue(refreshTokenValue)).thenReturn(Optional.of(expiredRefreshToken));

        assertThrows(RefreshTokenExpiredException.class, () -> authService.reissueToken(refreshTokenValue));

        verify(refreshTokenRepository, times(1)).delete(refreshTokenValue);
    }
}