package com.numble.instagram.util.fixture.dm;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.entity.Message;
import com.numble.instagram.domain.user.entity.User;

public class MessageFixture {

    public static Message create(String content, User fromUser, User toUser, ChatRoom chatRoom) {
        return Message.create(content, fromUser, toUser, chatRoom);
    }
}
