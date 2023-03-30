package com.numble.instagram.dto.response.auth;

public record AccessTokenResponse(String accessToken) {
    public static AccessTokenResponse from(String accessToken) {
        return new AccessTokenResponse(accessToken);
    }
}
