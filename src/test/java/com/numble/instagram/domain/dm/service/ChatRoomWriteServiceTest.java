package com.numble.instagram.domain.dm.service;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.repository.ChatRoomRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.util.fixture.dm.ChatRoomFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatRoomWriteServiceTest {

    @InjectMocks
    private ChatRoomWriteService chatRoomWriteService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Test
    @DisplayName("chatroom이 존재하면 가져온다. 새로 생성하지 않고")
    void createOrGet_existingChatRoom() {
        User user1 = UserFixture.create("user1");
        User user2 = UserFixture.create("user2");
        ChatRoom chatRoom = ChatRoomFixture.create(user1, user2);

        when(chatRoomRepository.findChatRoomByUsers(user1, user2)).thenReturn(Optional.of(chatRoom));

        ChatRoom result = chatRoomWriteService.createOrGet(user1, user2);

        assertEquals(chatRoom, result);
    }

    @Test
    @DisplayName("chatroom이 존재하지 않으면 새로 생성한다.")
    void createOrGet_newChatRoom() {
        User user1 = UserFixture.create("user1");
        User user2 = UserFixture.create("user2");
        ChatRoom chatRoom = ChatRoomFixture.create(user1, user2);

        when(chatRoomRepository.findChatRoomByUsers(user1, user2)).thenReturn(Optional.empty());
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);

        ChatRoom result = chatRoomWriteService.createOrGet(user1, user2);

        assertEquals(chatRoom, result);
    }
}