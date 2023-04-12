package com.numble.instagram.application.usecase.dm;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.entity.Message;
import com.numble.instagram.domain.dm.service.ChatRoomWriteService;
import com.numble.instagram.domain.dm.service.MessageWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.request.dm.MessageRequest;
import com.numble.instagram.dto.response.dm.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateMessageUsecase {

    private final UserReadService userReadService;
    private final MessageWriteService messageWriteService;
    private final ChatRoomWriteService chatRoomWriteService;

    @Transactional
    public MessageResponse execute(Long fromUserId, Long toUserId, MessageRequest messageRequest) {
        User fromUser = userReadService.getUser(fromUserId);
        User toUser = userReadService.getUser(toUserId);

        ChatRoom chatRoom = chatRoomWriteService.createOrGet(fromUser, toUser);
        Message newMessage = messageWriteService.create(fromUser, toUser, chatRoom, messageRequest.content());

        return MessageResponse.from(newMessage);
    }
}
