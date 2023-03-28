package com.numble.instagram.application.auth.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/refreshTokenDDL.sql")
class RefreshTokenRepositoryImplTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private RefreshTokenRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new RefreshTokenRepositoryImpl(jdbcTemplate);
    }

    @Test
    @DisplayName("RefreshToken을 저장한다.")
    void save_shouldInsertTokenIntoDatabase() {
        // given
        RefreshToken token = RefreshToken.builder()
                .refreshToken("abc123")
                .expiredAt(LocalDateTime.now())
                .userId(1L)
                .build();

        // when
        RefreshToken savedToken = repository.save(token);

        // then
        assertNotNull(savedToken.refreshToken());
        assertNotNull(savedToken.expiredAt());
        assertEquals(token.userId(), savedToken.userId());
    }

    @Test
    @DisplayName("저장된 토큰을 조회하면 존재해야한다.")
    void findTokenByTokenValue_shouldReturnTokenIfExists() {
        // given
        String tokenValue = "abc123";
        RefreshToken token = RefreshToken.builder()
                .refreshToken(tokenValue)
                .expiredAt(LocalDateTime.now())
                .userId(1L)
                .build();
        repository.save(token);

        // when
        Optional<RefreshToken> foundToken = repository.findTokenByTokenValue(tokenValue);

        // then
        assertTrue(foundToken.isPresent());
        assertEquals(tokenValue, foundToken.get().refreshToken());
    }

    @Test
    @DisplayName("저정되지 않은 토큰을 조회하면 없어야한다.")
    void findTokenByTokenValue_shouldReturnEmptyOptionalIfTokenDoesNotExist() {
        // given
        String tokenValue = "xyz456";

        // when
        Optional<RefreshToken> foundToken = repository.findTokenByTokenValue(tokenValue);

        // then
        assertTrue(foundToken.isEmpty());
    }

    @Test
    @DisplayName("저장된 토큰을 삭제하면 삭제되어야 한다.")
    void delete_shouldRemoveTokenFromDatabase() {
        // given
        String tokenValue = "abc123";
        RefreshToken token = RefreshToken.builder()
                .refreshToken(tokenValue)
                .expiredAt(LocalDateTime.now())
                .userId(1L)
                .build();
        repository.save(token);

        // when
        repository.delete(tokenValue);

        // then
        Optional<RefreshToken> foundToken = repository.findTokenByTokenValue(tokenValue);
        assertTrue(foundToken.isEmpty());
    }

    @Test
    @DisplayName("저장되지 않은 토큰을 삭제하려면 예외가 발생한다.")
    void delete_shouldThrowExceptionIfTokenDoesNotExist() {
        // given
        String tokenValue = "xyz456";

        // when & then
        assertThrows(IllegalStateException.class, () -> repository.delete(tokenValue));
    }
}