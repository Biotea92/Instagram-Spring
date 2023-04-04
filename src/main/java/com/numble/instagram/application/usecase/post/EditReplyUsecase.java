package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.post.entity.Reply;
import com.numble.instagram.domain.post.service.ReplyWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.request.post.ReplyRequest;
import com.numble.instagram.dto.response.post.ReplyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditReplyUsecase {

    private final UserReadService userReadService;
    private final ReplyWriteService replyWriteService;

    public ReplyResponse execute(Long userId, Long replyId, ReplyRequest replyRequest) {
        User user = userReadService.getUser(userId);
        Reply editedReply = replyWriteService.edit(user, replyId, replyRequest.content());

        return ReplyResponse.from(editedReply);
    }
}
