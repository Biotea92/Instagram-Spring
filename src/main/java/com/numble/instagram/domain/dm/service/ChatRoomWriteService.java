package com.numble.instagram.domain.dm.service;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.repository.ChatRoomRepository;
import com.numble.instagram.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomWriteService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createOrGet(User user1, User user2) {
        Optional<ChatRoom> findChatRoom = chatRoomRepository.findChatRoomByUsers(user1, user2);
        if (findChatRoom.isPresent()) {
            return findChatRoom.get();
        }
        ChatRoom chatRoom = ChatRoom.builder()
                .user1(user1)
                .user2(user2)
                .build();
        return chatRoomRepository.save(chatRoom);
    }
}
