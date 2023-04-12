package com.numble.instagram.exception.badrequest;

public class NotChatRoomUserException extends InvalidRequestException {

    public NotChatRoomUserException() {
        addValidation("user", "채팅방 유저가 아닙니다.");
    }
}
