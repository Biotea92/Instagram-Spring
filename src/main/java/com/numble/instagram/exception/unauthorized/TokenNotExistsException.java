package com.numble.instagram.exception.unauthorized;

public class TokenNotExistsException extends UnauthorizedException {

    public TokenNotExistsException() {
        addValidation("token", "토큰이 존재하지않습니다.");
    }
}
