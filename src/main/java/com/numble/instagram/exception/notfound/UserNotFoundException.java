package com.numble.instagram.exception.notfound;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        addValidation("user", "사용자를 찾을 수 없습니다.");
    }
}
