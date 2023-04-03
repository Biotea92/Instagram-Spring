package com.numble.instagram.domain.post.service;

import com.numble.instagram.dto.common.PostDto;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.repository.PostRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.exception.badrequest.NotPostWriterException;
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

import static org.junit.jupiter.api.Assertions.*;
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

        assertEquals(writerUser, newPost.getWriteUser());
        assertEquals(content, newPost.getContent());
        assertEquals(postImageUrl, newPost.getPostImageUrl());
    }

    @Test
    @DisplayName("글을 수정한다.")
    void edit() {
        User writerUser = UserFixture.create("writerUser");
        String newContent = "new content";
        String newImageUrl = "https://newImage.jpg";
        Post post = PostFixture.create("https://oldImage.jpg", "old content", writerUser);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        PostDto result = postWriteService.edit(writerUser, post.getId(), newContent, newImageUrl);

        assertEquals(post.getId(), result.post().getId());
        assertEquals(post.getContent(), newContent);
        assertEquals(post.getPostImageUrl(), newImageUrl);
        assertEquals("https://oldImage.jpg", result.willDeleteImageUrl());
    }

    @Test
    @DisplayName("글을 이미지없이 수정한다.")
    void editWithOnlyContent() {
        User writerUser = UserFixture.create("writerUser");
        String newContent = "new content";
        Post post = PostFixture.create("https://oldImage.jpg", "old content", writerUser);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        PostDto result = postWriteService.edit(writerUser, post.getId(), newContent, null);

        assertEquals(post.getId(), result.post().getId());
        assertEquals(post.getContent(), newContent);
        assertEquals("https://oldImage.jpg", result.post().getPostImageUrl());
        assertNull(result.willDeleteImageUrl());
    }

    @Test
    @DisplayName("post가 없으면 PostNotFoundException이 발생한다.")
    void postNotFound() {
        User writerUser = UserFixture.create("writerUser");
        Long nonExistingPostId = 2L;
        when(postRepository.findById(nonExistingPostId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class,
                () -> postWriteService.edit(writerUser, nonExistingPostId, "new content", null));
    }

    @Test
    @DisplayName("수정을 시도한 user가 writer이 아니면 NotPostWriterException이 발생한다.")
    void notPostWriter() {
        User writerUser = UserFixture.create("writerUser");
        Post post = PostFixture.create("https://oldImage.jpg", "old content", writerUser);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        User notWriter = UserFixture.create("notWriter");
        assertThrows(NotPostWriterException.class,
                () -> postWriteService.edit(notWriter, post.getId(), "new content", "http://newImage"));
    }
}