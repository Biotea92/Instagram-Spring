package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.repository.PostRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.exception.notfound.PostNotFoundException;
import com.numble.instagram.util.fixture.post.PostFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostReadServiceTest {

    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private PostReadService postReadService;

    @Test
    @DisplayName("포스트 조회는 완료되어야한다.")
    void getPost() {
        User writer = UserFixture.create("writer");
        Post post = PostFixture.create("imageUrl", "content", writer);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        Post result = postReadService.getPost(post.getId());

        assertEquals(post.getContent(), result.getContent());
        assertEquals(post.getPostImageUrl(), result.getPostImageUrl());
        assertEquals(post.getWriteUser(), result.getWriteUser());
    }

    @Test
    @DisplayName("포스트가 없으면 Exception이 발생한다.")
    void getPostWillPostNotFoundException() {
        User writer = UserFixture.create("writer");
        Post post = PostFixture.create("imageUrl", "content", writer);
        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postReadService.getPost(post.getId()));
    }
}