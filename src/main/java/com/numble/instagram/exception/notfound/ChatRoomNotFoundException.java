package com.numble.instagram.exception.notfound;

public class ChatRoomNotFoundException extends NotFoundException {

    public ChatRoomNotFoundException() {
        addValidation("chatroom", "채팅방을 찾을 수 없습니다.");
    }
}
