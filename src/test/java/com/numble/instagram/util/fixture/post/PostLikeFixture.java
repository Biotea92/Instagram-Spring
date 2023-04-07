package com.numble.instagram.util.fixture.post;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.entity.PostLike;
import com.numble.instagram.domain.user.entity.User;

public class PostLikeFixture {

    public static PostLike create(User user, Post post) {
        return new PostLike(user, post);
    }
}
