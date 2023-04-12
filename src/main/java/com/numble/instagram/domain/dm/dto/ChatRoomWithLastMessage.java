package com.numble.instagram.domain.dm.dto;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.entity.Message;

public record ChatRoomWithLastMessage(ChatRoom chatRoom, Message lastMessage) {
}
