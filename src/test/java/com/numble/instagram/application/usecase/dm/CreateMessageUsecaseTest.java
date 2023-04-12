package com.numble.instagram.application.usecase.dm;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.entity.Message;
import com.numble.instagram.domain.dm.service.ChatRoomWriteService;
import com.numble.instagram.domain.dm.service.MessageWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.request.dm.MessageRequest;
import com.numble.instagram.dto.response.dm.MessageResponse;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateMessageUsecaseTest {

    @InjectMocks
    private CreateMessageUsecase createMessageUsecase;
    @Mock
    private UserReadService userReadService;
    @Mock
    private MessageWriteService messageWriteService;
    @Mock
    private ChatRoomWriteService chatRoomWriteService;

    @Test
    @DisplayName("Message는 생성된다.")
    public void execute() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        User fromUser = UserFixture.create(fromUserId, "fromUser");
        User toUser = UserFixture.create(toUserId, "toUser");
        ChatRoom chatRoom = ChatRoomFixture.create(fromUser, toUser);
        MessageRequest messageRequest = new MessageRequest("new-message-content");
        Message message = MessageFixture.create(messageRequest.content(), fromUser, toUser, chatRoom);

        when(userReadService.getUser(fromUserId)).thenReturn(fromUser);
        when(userReadService.getUser(toUserId)).thenReturn(toUser);
        when(chatRoomWriteService.createOrGet(fromUser, toUser)).thenReturn(chatRoom);
        when(messageWriteService.create(fromUser, toUser, chatRoom, messageRequest.content())).thenReturn(message);

        MessageResponse result = createMessageUsecase.execute(fromUserId, toUserId, messageRequest);

        assertEquals(toUserId, result.userId());
        assertEquals(messageRequest.content(), result.content());
    }
}