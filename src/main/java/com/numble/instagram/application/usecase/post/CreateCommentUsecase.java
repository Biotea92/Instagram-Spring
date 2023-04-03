package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.service.CommentWriteService;
import com.numble.instagram.domain.post.service.PostReadService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.request.post.CommentRequest;
import com.numble.instagram.dto.response.post.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCommentUsecase {

    private final UserReadService userReadService;
    private final PostReadService postReadService;
    private final CommentWriteService commentWriteService;

    public CommentResponse execute(Long userId, Long postId, CommentRequest commentRequest) {
        User user = userReadService.getUser(userId);
        Post post = postReadService.getPost(postId);
        Comment newComment = commentWriteService.register(user, post, commentRequest.content());
        return CommentResponse.from(newComment);
    }
}
