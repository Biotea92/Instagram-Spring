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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetChatRoomWithMessagesUsecase {

    private final UserReadService userReadService;
    private final ChatRoomReadService chatRoomReadService;
    private final MessageReadService messageReadService;

    public PageCursor<MessageDetailResponse> execute(Long userId, Long chatroomId, CursorRequest cursorRequest) {
        User user = userReadService.getUser(userId);
        ChatRoom chatRoom = chatRoomReadService.getChatRoom(chatroomId, user);
        List<Message> messages = messageReadService.getMessages(chatRoom, cursorRequest);

        Long nextKey = messages.stream()
                .mapToLong(Message::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);

        List<MessageDetailResponse> messageDetailResponses = messages.stream()
                .map(MessageDetailResponse::from)
                .toList();

        return PageCursor.fromMessageDetailResponse(cursorRequest.next(nextKey), messageDetailResponses);
    }
}
