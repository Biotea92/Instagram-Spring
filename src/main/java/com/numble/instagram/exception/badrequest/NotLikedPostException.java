package com.numble.instagram.exception.badrequest;

public class NotLikedPostException extends InvalidRequestException {

    public NotLikedPostException() {
        addValidation("post", "좋아요 한 글이 아닙니다.");
    }
}
