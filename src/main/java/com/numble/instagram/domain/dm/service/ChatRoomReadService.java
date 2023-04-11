package com.numble.instagram.domain.dm.service;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.repository.ChatRoomRepository;
import com.numble.instagram.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomReadService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom getChatRoom(User user1, User user2) {
        return chatRoomRepository.findChatRoomByUsers(user1, user2)
                .orElseThrow();
    }
}
