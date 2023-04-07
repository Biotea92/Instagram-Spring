package com.numble.instagram.dto.response.post;

import com.numble.instagram.domain.post.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CommentDetailResponse(
        Long commentId,
        String content,
        LocalDateTime createdAt,
        Long userId,
        String nickname,
        String profileImageUrl,
        List<ReplyDetailResponse> replies) {

    public static CommentDetailResponse from(Comment comment) {
        return CommentDetailResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .userId(comment.getCommentWriteUser().getId())
                .nickname(comment.getCommentWriteUser().getNickname())
                .profileImageUrl(comment.getCommentWriteUser().getProfileImageUrl())
                .replies(comment.getReplies().stream().map(ReplyDetailResponse::from).toList()  )
                .build();
    }
}
