package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.post.service.ReplyWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DestroyReplyUsecase {

    private final UserReadService userReadService;
    private final ReplyWriteService replyWriteService;

    public void execute(Long userId, Long replyId) {
        User user = userReadService.getUser(userId);
        replyWriteService.deleteReply(user, replyId);

    }
}
