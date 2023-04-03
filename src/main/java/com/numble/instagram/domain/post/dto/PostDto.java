package com.numble.instagram.domain.post.dto;

import com.numble.instagram.domain.post.entity.Post;

public record PostDto(Post post, String willDeleteImageUrl) {

    public PostDto(Post post) {
        this(post, null);
    }

    public static PostDto from(Post post, String willDeleteImageUrl) {
        return new PostDto(post, willDeleteImageUrl);
    }

    public static PostDto from(Post post) {
        return new PostDto(post);
    }
}
