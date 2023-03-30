package com.numble.instagram.dto.common;

public record LoginDto(String accessToken, String refreshToken) {

    public static LoginDto create(String accessToken, String refreshToken) {
        return new LoginDto(accessToken, refreshToken);
    }
}
