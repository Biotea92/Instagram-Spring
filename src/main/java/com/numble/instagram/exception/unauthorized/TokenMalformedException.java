package com.numble.instagram.exception.unauthorized;

public class TokenMalformedException extends UnauthorizedException {

    public TokenMalformedException() {
        addValidation("token", "토큰 형식이 올바르지 않습니다.");
    }
}
