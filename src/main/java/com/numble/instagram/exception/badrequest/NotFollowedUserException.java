package com.numble.instagram.exception.badrequest;

public class NotFollowedUserException extends InvalidRequestException {

    public NotFollowedUserException() {
        addValidation("toUserId", "팔로우 한 유저가 아닙니다.");
    }
}
