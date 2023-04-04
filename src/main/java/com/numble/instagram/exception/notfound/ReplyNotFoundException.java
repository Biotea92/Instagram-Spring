package com.numble.instagram.exception.notfound;

public class ReplyNotFoundException extends NotFoundException {

    public ReplyNotFoundException() {
        addValidation("reply", "reply를 찾을 수 없습니다.");
    }
}
