package com.numble.instagram.util.fixture.dm;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.user.entity.User;

public class ChatRoomFixture {

    public static ChatRoom create(User user1, User user2) {
        return new ChatRoom(user1, user2);
    }
}
