package com.numble.instagram.dto.response.post;

public record PostLikeResponse(Long postId) {

    public static PostLikeResponse from(Long postId) {
        return new PostLikeResponse(postId);
    }
}
