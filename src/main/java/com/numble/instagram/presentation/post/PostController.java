package com.numble.instagram.presentation.post;

import com.numble.instagram.application.usecase.post.CreateCommentUsecase;
import com.numble.instagram.application.usecase.post.CreatePostUsecase;
import com.numble.instagram.application.usecase.post.EditPostUsecase;
import com.numble.instagram.dto.request.post.CommentCreateRequest;
import com.numble.instagram.dto.request.post.PostCreateRequest;
import com.numble.instagram.dto.request.post.PostEditRequest;
import com.numble.instagram.dto.response.post.CommentResponse;
import com.numble.instagram.dto.response.post.PostResponse;
import com.numble.instagram.presentation.auth.AuthenticatedUser;
import com.numble.instagram.presentation.auth.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final CreatePostUsecase createPostUsecase;
    private final EditPostUsecase editPostUsecase;
    private final CreateCommentUsecase createCommentUsecase;

    @Login
    @PostMapping
    public PostResponse register(@AuthenticatedUser Long userId,
                                 @Validated PostCreateRequest postCreateRequest) {
        return createPostUsecase.execute(userId, postCreateRequest);
    }

    @Login
    @PostMapping("/{postId}/edit")
    public PostResponse edit(@AuthenticatedUser Long userId,
                             @PathVariable Long postId,
                             @Validated PostEditRequest postEditRequest) {
        return editPostUsecase.execute(userId, postId, postEditRequest);
    }

    @Login
    @PostMapping("/{postId}/comment")
    public CommentResponse commentRegister(@AuthenticatedUser Long userId,
                                           @PathVariable Long postId,
                                           @Validated CommentCreateRequest commentCreateRequest) {
        return createCommentUsecase.execute(userId, postId, commentCreateRequest);
    }
}
