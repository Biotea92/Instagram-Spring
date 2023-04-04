package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.service.CommentWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.request.post.CommentRequest;
import com.numble.instagram.dto.response.post.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditCommentUsecase {

    private final UserReadService userReadService;
    private final CommentWriteService commentWriteService;

    public CommentResponse execute(Long userId, Long commentId, CommentRequest commentRequest) {
        User user = userReadService.getUser(userId);
        Comment editedComment = commentWriteService.edit(user, commentId, commentRequest.content());

        return CommentResponse.from(editedComment);
    }
}
