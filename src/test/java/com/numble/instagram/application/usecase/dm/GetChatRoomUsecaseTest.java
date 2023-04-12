package com.numble.instagram.application.usecase.dm;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.entity.Message;
import com.numble.instagram.domain.dm.service.ChatRoomReadService;
import com.numble.instagram.domain.dm.service.MessageReadService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.response.dm.MessageDetailResponse;
import com.numble.instagram.support.paging.CursorRequest;
import com.numble.instagram.support.paging.PageCursor;
import com.numble.instagram.util.fixture.dm.ChatRoomFixture;
import com.numble.instagram.util.fixture.dm.MessageFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetChatRoomWithMessagesUsecaseUsecaseTest {

    @InjectMocks
    private GetChatRoomWithMessagesUsecase getChatRoomWithMessagesUsecase;
    @Mock
    private UserReadService userReadService;
    @Mock
    private ChatRoomReadService chatRoomReadService;
    @Mock
    private MessageReadService messageReadService;

    @Test
    @DisplayName("채팅방과 메시지들을 가져온다.")
    void getChatRoomWithMessages() {
        User user1 = UserFixture.create("User1");
        User user2 = UserFixture.create("User2");
        ChatRoom chatRoom = ChatRoomFixture.create(user1, user2);
        Message message = MessageFixture.create(1L);
        CursorRequest cursorRequest = new CursorRequest(1L, 10);

        when(userReadService.getUser(user1.getId())).thenReturn(user1);
        when(chatRoomReadService.getChatRoom(chatRoom.getId(), user1)).thenReturn(chatRoom);
        when(messageReadService.getMessages(chatRoom, cursorRequest)).thenReturn(Collections.singletonList(message));

        PageCursor<MessageDetailResponse> result = getChatRoomWithMessagesUsecase
                .execute(user1.getId(), chatRoom.getId(), cursorRequest);

        assertEquals(1, result.body().size());
        assertEquals(MessageDetailResponse.from(message), result.body().get(0));
        assertEquals(1L, result.nextCursorRequest().key());
    }
}