package com.numble.instagram.dto.response.post;

import com.numble.instagram.domain.post.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponse(Long id, String content, LocalDateTime createdAt) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getContent(), comment.getCreatedAt());
    }
}
