package com.numble.instagram.domain.dm.service;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.entity.Message;
import com.numble.instagram.domain.dm.repository.MessageRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.support.paging.CursorRequest;
import com.numble.instagram.util.fixture.dm.ChatRoomFixture;
import com.numble.instagram.util.fixture.dm.MessageFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MessageReadServiceTest {

    @InjectMocks
    private MessageReadService messageReadService;
    @Mock
    private MessageRepository messageRepository;

    @Test
    @DisplayName("메시지들을 가져온다.")
    void getMessages() {
        User chatRoomUser1 = UserFixture.create("chatRoomUser1");
        User chatRoomUser2 = UserFixture.create("chatRoomUser2");
        ChatRoom chatRoom = ChatRoomFixture.create(chatRoomUser1, chatRoomUser2);
        CursorRequest cursorRequest = new CursorRequest(null, 10);

        Message message = MessageFixture.create("message", chatRoomUser1, chatRoomUser2, chatRoom);

        Mockito.when(messageRepository.findAllByLessThanIdAndChatRoom(chatRoom, cursorRequest))
                .thenReturn(Collections.singletonList(message));

        List<Message> messages = messageReadService.getMessages(chatRoom, cursorRequest);

        Assertions.assertEquals(Collections.singletonList(message), messages);
    }
}