package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.repository.PostRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.util.fixture.post.PostFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostWriteServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostWriteService postWriteService;

    @Test
    @DisplayName("글을 등록한다.")
    void register() {
        User writerUser = UserFixture.create("writerUser");
        String content = "post content";
        String postImageUrl = "http://example.com/image.jpg";
        when(postRepository.save(any(Post.class))).thenReturn(PostFixture.create(postImageUrl, content, writerUser));

        Post newPost = postWriteService.register(writerUser, content, postImageUrl);

        assertEquals(writerUser, newPost.getWriterUser());
        assertEquals(content, newPost.getContent());
        assertEquals(postImageUrl, newPost.getPostImageUrl());
    }
}