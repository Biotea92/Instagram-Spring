package com.numble.instagram.presentation.dm;

import com.numble.instagram.application.usecase.dm.CreateMessageUsecase;
import com.numble.instagram.application.usecase.dm.GetChatRoomUsecase;
import com.numble.instagram.dto.request.dm.MessageRequest;
import com.numble.instagram.dto.response.dm.MessageDetailResponse;
import com.numble.instagram.dto.response.dm.MessageResponse;
import com.numble.instagram.presentation.auth.AuthenticatedUser;
import com.numble.instagram.presentation.auth.Login;
import com.numble.instagram.support.paging.CursorRequest;
import com.numble.instagram.support.paging.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dm")
public class DmController {

    private final CreateMessageUsecase createMessageUsecase;
    private final GetChatRoomUsecase getChatRoomUsecase;

    @Login
    @PostMapping("/{toUserId}")
    public MessageResponse sendMessage(@AuthenticatedUser Long fromUserId,
                                       @PathVariable Long toUserId,
                                       @RequestBody MessageRequest messageRequest) {
        return createMessageUsecase.execute(fromUserId, toUserId, messageRequest);
    }

    @Login
    @GetMapping("/chatroom/{chatroomId}")
    public PageCursor<MessageDetailResponse> getChatRoom(@AuthenticatedUser Long userId,
                                                         @PathVariable Long chatroomId,
                                                         CursorRequest cursorRequest) {
        return getChatRoomUsecase.execute(userId, chatroomId, cursorRequest);
    }
}
