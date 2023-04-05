package com.numble.instagram.presentation.post;

import com.numble.instagram.application.usecase.post.*;
import com.numble.instagram.dto.request.post.CommentRequest;
import com.numble.instagram.dto.request.post.PostCreateRequest;
import com.numble.instagram.dto.request.post.PostEditRequest;
import com.numble.instagram.dto.request.post.ReplyRequest;
import com.numble.instagram.dto.response.post.CommentResponse;
import com.numble.instagram.dto.response.post.PostLikeResponse;
import com.numble.instagram.dto.response.post.PostResponse;
import com.numble.instagram.dto.response.post.ReplyResponse;
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
    private final CreateReplyUsecase createReplyUsecase;
    private final EditReplyUsecase editReplyUsecase;
    private final CreatePostLikeUsecase createPostLikeUsecase;
    private final DestroyPostLikeUsecase destroyPostLikeUsecase;

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

    @Login
    @PostMapping("/comment/{commentId}/reply")
    public ReplyResponse replyRegister(@AuthenticatedUser Long userId,
                                       @PathVariable Long commentId,
                                       @Validated @RequestBody ReplyRequest replyRequest) {
        return createReplyUsecase.execute(userId, commentId, replyRequest);
    }

    @Login
    @PutMapping("/comment/reply/{replyId}")
    public ReplyResponse replyEdit(@AuthenticatedUser Long userId,
                                   @PathVariable Long replyId,
                                   @Validated @RequestBody ReplyRequest replyRequest) {
        return editReplyUsecase.execute(userId, replyId, replyRequest);
    }

    @Login
    @PostMapping("/{postId}/like")
    public PostLikeResponse likePost(@AuthenticatedUser Long userId,
                                     @PathVariable Long postId) {
        return createPostLikeUsecase.execute(userId, postId);
    }

    @Login
    @PostMapping("/{postId}/dislike")
    public PostLikeResponse dislikePost(@AuthenticatedUser Long userId,
                                        @PathVariable Long postId) {
        return destroyPostLikeUsecase.execute(userId, postId);
    }
}
