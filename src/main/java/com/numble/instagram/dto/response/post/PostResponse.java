package com.numble.instagram.dto.response.post;

import com.numble.instagram.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostResponse(Long id, String postImageUrl, String content, Long likeCount, LocalDateTime createdAt) {

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(), post.getPostImageUrl(), post.getContent(), post.getLikeCount(), post.getCreatedAt());
    }
}
