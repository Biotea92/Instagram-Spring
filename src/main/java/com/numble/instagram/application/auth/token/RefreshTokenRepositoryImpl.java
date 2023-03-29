package com.numble.instagram.application.auth.token;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String TABLE_NAME = "refresh_token";
    private static final RowMapper<RefreshToken> rowMapper = (rs, rowNum) -> RefreshToken.builder()
            .tokenValue(rs.getString("token_value"))
            .expiredAt(rs.getTimestamp("expired_at").toLocalDateTime())
            .userId(rs.getLong("user_id"))
            .build();

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        String sql = String.format("""
                INSERT INTO %s (token_value, expired_at, user_id)
                VALUES (:tokenValue, :expiredAt, :userId)
                """, TABLE_NAME);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(refreshToken);
        int insertCount = namedParameterJdbcTemplate.update(sql, parameterSource);
        if (insertCount == 1) {
            return refreshToken;
        }
        throw new IllegalStateException();
    }

    @Override
    public Optional<RefreshToken> findTokenByTokenValue(String tokenValue) {
        String sql = String.format("""
                SELECT token_value, expired_at, user_id
                FROM %s
                WHERE token_value = :token_value
                """, TABLE_NAME);
        SqlParameterSource namedParameters = new MapSqlParameterSource("token_value", tokenValue);
        List<RefreshToken> results = namedParameterJdbcTemplate.query(sql, namedParameters, rowMapper);
        if (results.size() > 1) {
            throw new IllegalStateException();
        }
        return results.stream().findFirst();
    }

    @Override
    public void delete(String tokenValue) {
        final String sql = String.format("""
                DELETE FROM %s
                WHERE token_value = :token_value
                """, TABLE_NAME);
        SqlParameterSource namedParameters = new MapSqlParameterSource("token_value", tokenValue);
        int deleteCount = namedParameterJdbcTemplate.update(sql, namedParameters);
        if (deleteCount != 1) {
            throw new IllegalStateException();
        }
    }
}
