package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Feed;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.repository.FeedRepository;
import com.numble.instagram.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedWriteService {

    private final FeedRepository feedRepository;

    public void deliveryToFeed(Post newPost, List<User> followers) {
        List<Feed> feeds = followers.stream()
                .map(follower -> toFeed(newPost, follower))
                .toList();

        // TODO bulkInsert 하는 방법으로 고려해볼 것
        feedRepository.saveAll(feeds);
    }

    public void deleteFeed(Post post) {
        List<Feed> feeds = feedRepository.findAllByPost(post);
        feedRepository.deleteAll(feeds);
    }

    private Feed toFeed(Post newPost, User follower) {
        return Feed.builder()
                .user(follower)
                .post(newPost)
                .createdAt(newPost.getCreatedAt())
                .build();
    }
}
