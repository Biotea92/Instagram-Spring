package com.numble.instagram.domain.dm.repository;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.entity.Message;
import com.numble.instagram.support.paging.CursorRequest;

import java.util.List;

public interface MessageRepositoryCustom {

    List<Message> findAllByLessThanIdAndChatRoom(ChatRoom chatRoom, CursorRequest request);
}
