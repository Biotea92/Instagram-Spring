package com.numble.instagram.exception.unauthorized;

public class RefreshTokenNotExistsException extends UnauthorizedException {

    public RefreshTokenNotExistsException() {
        addValidation("refreshToken", "토큰이 존재하지않습니다.");
    }
}
