package com.numble.instagram.domain.dm.service;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.entity.Message;
import com.numble.instagram.domain.dm.repository.MessageRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.util.fixture.dm.ChatRoomFixture;
import com.numble.instagram.util.fixture.dm.MessageFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageWriteServiceTest {

    @InjectMocks
    private MessageWriteService messageWriteService;

    @Mock
    private MessageRepository messageRepository;

    @Test
    @DisplayName("Message는 생성된다.")
    void createMessage() {
        User fromUser = UserFixture.create("fromUser");
        User toUser = UserFixture.create("toUser");
        ChatRoom chatRoom = ChatRoomFixture.create(fromUser, toUser);
        String content = "new-message-content";

        Message message = MessageFixture.create(content, fromUser, toUser, chatRoom);

        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message result = messageWriteService.create(fromUser, toUser, chatRoom, content);

        assertEquals(message, result);
    }
}