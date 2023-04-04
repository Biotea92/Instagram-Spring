package com.numble.instagram.presentation.post;

import com.numble.instagram.application.usecase.post.CreateCommentUsecase;
import com.numble.instagram.application.usecase.post.CreatePostUsecase;
import com.numble.instagram.application.usecase.post.EditCommentUsecase;
import com.numble.instagram.application.usecase.post.EditPostUsecase;
import com.numble.instagram.dto.request.post.CommentRequest;
import com.numble.instagram.dto.request.post.PostCreateRequest;
import com.numble.instagram.dto.request.post.PostEditRequest;
import com.numble.instagram.dto.response.post.CommentResponse;
import com.numble.instagram.dto.response.post.PostResponse;
import com.numble.instagram.presentation.auth.AuthenticatedUser;
import com.numble.instagram.presentation.auth.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final CreatePostUsecase createPostUsecase;
    private final EditPostUsecase editPostUsecase;
    private final CreateCommentUsecase createCommentUsecase;
    private final EditCommentUsecase editCommentUsecase;

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
                                           @Validated @RequestBody CommentRequest commentRequest) {
        return createCommentUsecase.execute(userId, postId, commentRequest);
    }

    @Login
    @PutMapping("/comment/{commentId}")
    public CommentResponse commentEdit(@AuthenticatedUser Long userId,
                                       @PathVariable Long commentId,
                                       @Validated @RequestBody CommentRequest commentRequest) {
        return editCommentUsecase.execute(userId, commentId, commentRequest);
    }
}
