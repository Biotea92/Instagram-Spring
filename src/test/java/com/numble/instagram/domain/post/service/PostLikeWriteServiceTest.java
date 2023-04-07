package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.entity.PostLike;
import com.numble.instagram.domain.post.repository.PostLikeRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.exception.badrequest.AlreadyLikedPostException;
import com.numble.instagram.exception.badrequest.NotLikedPostException;
import com.numble.instagram.util.fixture.post.PostFixture;
import com.numble.instagram.util.fixture.post.PostLikeFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostLikeWriteServiceTest {

    @Mock
    private PostLikeRepository postLikeRepository;
    @InjectMocks
    private PostLikeWriteService postLikeWriteService;

    @Test
    @DisplayName("포스트 좋아요")
    void like() {
        User user = UserFixture.create("user");
        Post post = PostFixture.create("post-content");
        when(postLikeRepository.existsByUserAndPost(user, post)).thenReturn(false);
        postLikeWriteService.like(user, post);
        verify(postLikeRepository, times(1)).save(any(PostLike.class));
    }

    @Test
    @DisplayName("이미 좋아요 한 포스트")
    void likeAlreadyLiked() {
        User user = UserFixture.create("user");
        Post post = PostFixture.create("post-content");
        when(postLikeRepository.existsByUserAndPost(user, post)).thenReturn(true);
        assertThrows(AlreadyLikedPostException.class, () -> postLikeWriteService.like(user, post));
    }

    @Test
    @DisplayName("좋아요 취소")
    void dislike() {
        User user = UserFixture.create("user");
        Post post = PostFixture.create("post-content");
        PostLike postLike = PostLikeFixture.create(user, post);
        when(postLikeRepository.findByUserAndPost(user, post)).thenReturn(Optional.of(postLike));
        postLikeWriteService.dislike(user, post);
        verify(postLikeRepository, times(1)).delete(postLike);
    }

    @Test
    @DisplayName("이미 좋아요가 되어있지 않음일때 좋아요 취소")
    void dislikeNotLiked() {
        User user = UserFixture.create("user");
        Post post = PostFixture.create("post-content");
        when(postLikeRepository.findByUserAndPost(user, post)).thenReturn(Optional.empty());
        assertThrows(NotLikedPostException.class, () -> postLikeWriteService.dislike(user, post));
    }
}