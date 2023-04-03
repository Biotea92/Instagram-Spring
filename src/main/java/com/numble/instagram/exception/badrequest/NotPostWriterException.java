package com.numble.instagram.exception.badrequest;

public class NotPostWriterException extends InvalidRequestException {

    public NotPostWriterException() {
        addValidation("user", "글 작성자가 아닙니다.");
    }
}
