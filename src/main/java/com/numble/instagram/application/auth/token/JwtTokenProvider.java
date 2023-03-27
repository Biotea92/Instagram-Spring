package com.numble.instagram.application.auth.token;

import com.numble.instagram.exception.unauthorized.TokenExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider implements TokenProvider {

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private final AuthTokenExtractor authTokenExtractor;
    private final Key secretKey;
    private final long validTimeInMilliseconds;

    public JwtTokenProvider(
            AuthTokenExtractor authTokenExtractor,
            @Value("${jwt.token.secret}") String secretKey,
            @Value("${jwt.token.valid-time-in-milliseconds}") long validTimeInMilliseconds) {
        this.authTokenExtractor = authTokenExtractor;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validTimeInMilliseconds = validTimeInMilliseconds;
    }

    @Override
    public String createAccessToken(Long id) {
        Date now = new Date();
        Date validTime = new Date(now.getTime() + validTimeInMilliseconds);

        return Jwts.builder()
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setIssuedAt(now)
                .setExpiration(validTime)
                .claim("id", id)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isValidToken(String authorizationHeader) {
        String token = authTokenExtractor.extractToken(authorizationHeader);
        try {
            Jws<Claims> claims = getClaims(token);
            return isAccessToken(claims) && isNotExpired(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Long getUserId(String authorizationHeader) {
        String token = authTokenExtractor.extractToken(authorizationHeader);
        Claims body = getClaims(token).getBody();
        try {
            return body.get("id", Long.class);
        } catch (RequiredTypeException | NullPointerException | IllegalArgumentException e) {
            throw new TokenExpiredException();
        }
    }


    private Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

    private boolean isAccessToken(Jws<Claims> claims) {
        return claims.getBody()
                .getSubject()
                .equals(ACCESS_TOKEN_SUBJECT);
    }

    private boolean isNotExpired(Jws<Claims> claims) {
        return claims.getBody()
                .getExpiration()
                .after(new Date());
    }
}
