package com.numble.instagram.application.usecase.dm;

import com.numble.instagram.domain.dm.dto.ChatRoomWithLastMessage;
import com.numble.instagram.domain.dm.service.ChatRoomReadService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.response.dm.ChatRoomWithMessageResponse;
import com.numble.instagram.support.paging.CursorRequest;
import com.numble.instagram.support.paging.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetChatRoomsUsecase {

    private final UserReadService userReadService;
    private final ChatRoomReadService chatRoomReadService;

    public PageCursor<ChatRoomWithMessageResponse> execute(Long userId, CursorRequest cursorRequest) {
        User user = userReadService.getUser(userId);
        List<ChatRoomWithLastMessage> chatRoomWithLastMessages =
                chatRoomReadService.getChatRoomsWithLastMessage(user, cursorRequest);

        Long nextKey = chatRoomWithLastMessages.stream()
                .mapToLong(cm -> cm.chatRoom().getId())
                .min()
                .orElse(CursorRequest.NONE_KEY);

        List<ChatRoomWithMessageResponse> chatRoomWithMessageResponses = chatRoomWithLastMessages.stream()
                .map(cm -> ChatRoomWithMessageResponse.from(cm.chatRoom(), cm.lastMessage()))
                .toList();

        return PageCursor.fromChatRoomWithMessageResponse(cursorRequest.next(nextKey), chatRoomWithMessageResponses);
    }
}
