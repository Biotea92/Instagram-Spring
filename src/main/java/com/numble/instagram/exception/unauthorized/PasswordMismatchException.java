package com.numble.instagram.exception.unauthorized;

public class PasswordMismatchException extends UnauthorizedException {

    public PasswordMismatchException() {
        addValidation("password", "패스워드가 일치하지 않습니다.");
    }
}
