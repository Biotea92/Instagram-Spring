package com.numble.instagram.domain.dm.repository;

import com.numble.instagram.domain.dm.dto.ChatRoomWithLastMessage;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.support.paging.CursorRequest;

import java.util.List;

public interface ChatRoomRepositoryCustom {

    List<ChatRoomWithLastMessage> findChatRoomsByUserWithLatestMessage(User user, CursorRequest cursorRequest);
}
