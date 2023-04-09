package com.numble.instagram.util.fixture.post;

import com.numble.instagram.domain.post.entity.Feed;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.user.entity.User;

import java.util.List;

public class FeedFixture {

    public static Feed create(User user, Post post) {
        return new Feed(user, post, post.getCreatedAt());
    }

    public static List<Feed> createFeeds(User user, List<Post> posts) {
        return posts.stream()
                .map(post -> create(user, post))
                .toList();
    }
}
