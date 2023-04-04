package com.numble.instagram.exception.badrequest;

public class NotReplyWriterException extends InvalidRequestException {

    public NotReplyWriterException() {
        addValidation("user", "답글 작성자가 아닙니다.");
    }
}
