package com.numble.instagram.dto.response.auth;

public record LoginResponse(String accessToken) {
    public static LoginResponse from(String accessToken) {
        return new LoginResponse(accessToken);
    }
}
