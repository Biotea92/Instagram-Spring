package com.numble.instagram.exception.badrequest;

public class AlreadyFollowedUserException extends InvalidRequestException {

    public AlreadyFollowedUserException() {
        addValidation("toUserId", "이미 팔로우한 유저입니다.");
    }
}
