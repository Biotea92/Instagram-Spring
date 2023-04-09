package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.repository.PostLikeRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.util.fixture.post.PostFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostLikeReadServiceTest {

    @InjectMocks
    private PostLikeReadService postLikeReadService;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Test
    void getPostLike() {
        User user = UserFixture.create("user");
        Post post1 = PostFixture.create("post-content1");
        Post post2 = PostFixture.create("post-content2");
        List<Post> posts = List.of(post1, post2);

        when(postLikeRepository.findAllByUserAndPosts(eq(user), eq(posts))).thenReturn(Arrays.asList(true, false));

        List<Boolean> isPostLike = postLikeReadService.getPostLikes(user, posts);

        assertThat(isPostLike.get(0)).isFalse();
        assertThat(isPostLike.get(1)).isTrue();
    }
}