package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.repository.FeedRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.util.fixture.post.PostFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class FeedWriteServiceTest {

    @Mock
    private FeedRepository feedRepository;

    @InjectMocks
    private FeedWriteService feedWriteService;

    @Test
    @DisplayName("post는 feed로 배달된다.")
    void deliveryToFeed() {
        User writerUser = UserFixture.create("writerUser");
        List<User> followers = new LinkedList<>();
        IntStream.range(1, 20).forEach(i -> {
            User user = UserFixture.create("user" + i);
            followers.add(user);
        });

        Post newPost = PostFixture.create("imageUrl", "content", writerUser);

        feedWriteService.deliveryToFeed(newPost, followers);

        Mockito.verify(feedRepository).saveAll(any(List.class));
    }
}