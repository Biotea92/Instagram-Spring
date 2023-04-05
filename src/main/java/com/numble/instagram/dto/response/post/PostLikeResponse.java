package com.numble.instagram.dto.response.post;

import com.numble.instagram.domain.post.entity.PostLike;

public record PostLikeResponse(Long postId) {

    public static PostLikeResponse from(PostLike postLike) {
        return new PostLikeResponse(postLike.getPost().getId());
    }
}
