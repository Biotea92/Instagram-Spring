package com.numble.instagram.domain.dm.service;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.repository.ChatRoomRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.exception.badrequest.NotChatRoomUserException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatRoomReadServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;
    @InjectMocks
    private ChatRoomReadService chatRoomReadService;

    @Test
    @DisplayName("채팅방을 가져온다. user가 채팅방 user 일때")
    void getChatRoom() {
        User chatRoomUser1 = UserFixture.create("chatRoomUser1");
        User chatRoomUser2 = UserFixture.create("chatRoomUser2");
        ChatRoom chatRoom = ChatRoomFixture.create(chatRoomUser1, chatRoomUser2);

        when(chatRoomRepository.findById(chatRoom.getId())).thenReturn(Optional.of(chatRoom));

        ChatRoom result = chatRoomReadService.getChatRoom(chatRoom.getId(), chatRoomUser1);

        assertEquals(result, chatRoom);
    }

    @Test
    @DisplayName("채팅방을 가져오지 못한다. user가 채팅방 user가 아닐때")
    void getChatRoomWithNotChatRoomUser() {
        User chatRoomUser1 = UserFixture.create("chatRoomUser1");
        User chatRoomUser2 = UserFixture.create("chatRoomUser2");
        User notChatRoomUser = UserFixture.create("NotChatRoomUser");
        ChatRoom chatRoom = ChatRoomFixture.create(chatRoomUser1, chatRoomUser2);

        when(chatRoomRepository.findById(chatRoom.getId())).thenReturn(Optional.of(chatRoom));

        assertThrows(NotChatRoomUserException.class, () -> chatRoomReadService.getChatRoom(chatRoom.getId(), notChatRoomUser));
    }
}