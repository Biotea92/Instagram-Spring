package com.numble.instagram.application.auth.token;

import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findToken(String savedTokenUuid);

    void delete(String savedTokenUuid);
}
