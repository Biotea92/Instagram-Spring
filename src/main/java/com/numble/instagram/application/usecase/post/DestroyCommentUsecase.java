package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.post.service.CommentWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DestroyCommentUsecase {

    private final UserReadService userReadService;
    private final CommentWriteService commentWriteService;

    public void execute(Long userId, Long commentId) {
        User user = userReadService.getUser(userId);
        commentWriteService.deleteComment(user, commentId);
    }
}
