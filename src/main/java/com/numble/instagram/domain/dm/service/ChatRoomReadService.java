package com.numble.instagram.domain.dm.service;

import com.numble.instagram.domain.dm.dto.ChatRoomWithLastMessage;
import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.repository.ChatRoomRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.exception.badrequest.NotChatRoomUserException;
import com.numble.instagram.exception.notfound.ChatRoomNotFoundException;
import com.numble.instagram.support.paging.CursorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomReadService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom getChatRoom(User user1, User user2) {
        return chatRoomRepository.findChatRoomByUsers(user1, user2)
                .orElseThrow(ChatRoomNotFoundException::new);
    }

    public ChatRoom getChatRoom(Long chatroomId, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatroomId)
                .orElseThrow(ChatRoomNotFoundException::new);
        checkChatRoomUser(user, chatRoom);
        return chatRoom;
    }

    public List<ChatRoomWithLastMessage> getChatRoomsWithLastMessage(User user, CursorRequest cursorRequest) {
        return chatRoomRepository.findChatRoomsByUserWithLatestMessage(user, cursorRequest);
    }

    private static void checkChatRoomUser(User user, ChatRoom chatRoom) {
        if (!chatRoom.isChatRoomUser(user)) {
            throw new NotChatRoomUserException();
        }
    }
}
