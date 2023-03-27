package com.numble.instagram.application.auth.token;

public interface TokenProvider {

    String createAccessToken(Long id);

    boolean isValidToken(String authorizationHeader);

    Long getUserId(String authorizationHeader);
}
