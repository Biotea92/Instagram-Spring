package com.numble.instagram.exception.badrequest;

public class NotCommentWriterException extends InvalidRequestException {

    public NotCommentWriterException() {
        addValidation("user", "댓글 작성자가 아닙니다.");
    }
}
