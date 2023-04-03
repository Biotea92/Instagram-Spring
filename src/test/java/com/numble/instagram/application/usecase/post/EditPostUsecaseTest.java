package com.numble.instagram.application.usecase.post;

import com.numble.instagram.dto.common.PostDto;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.service.PostWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.request.post.PostEditRequest;
import com.numble.instagram.dto.response.post.PostResponse;
import com.numble.instagram.support.file.FileStore;
import com.numble.instagram.util.fixture.post.PostFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditPostUsecaseTest {

    @Mock
    private UserReadService userReadService;

    @Mock
    private PostWriteService postWriteService;

    @Mock
    private FileStore fileStore;

    @InjectMocks
    private EditPostUsecase editPostUsecase;

    @Test
    @DisplayName("이미지가 있을 때 글 수정")
    void executeWithImage() {
        Long userId = 1L;
        Long postId = 2L;
        String newContent = "new content";
        String newImageUrl = "https://new-image.jpg";
        MultipartFile postImageFile = new MockMultipartFile("test-image.jpg", "test.jpg".getBytes());
        PostEditRequest postEditRequest = new PostEditRequest("new content", postImageFile);
        User writer = UserFixture.create(userId, "writer");
        Post editedPost = PostFixture.create("https://new-image.jpg", newContent, writer);
        PostDto editedPostDto = PostDto.from(editedPost, "https://old-image.jpg");


        when(userReadService.getUser(userId)).thenReturn(writer);
        when(postWriteService.edit(writer, postId, newContent, newImageUrl)).thenReturn(editedPostDto);
        when(fileStore.uploadImage(postImageFile)).thenReturn(newImageUrl);

        PostResponse postResponse = editPostUsecase.execute(userId, postId, postEditRequest);

        assertEquals(newContent, postResponse.content());
        assertEquals(newImageUrl, postResponse.postImageUrl());
    }

    @Test
    @DisplayName("이미지가 없을 때 글 수정")
    void executeWithoutImage() {
        Long userId = 1L;
        Long postId = 2L;
        String newContent = "new content";
        PostEditRequest postEditRequest = new PostEditRequest("new content", null);
        User writer = UserFixture.create(userId, "writer");
        String oldImageUrl = "https://old-image.jpg";
        Post editedPost = PostFixture.create(oldImageUrl, newContent, writer);
        PostDto editedPostDto = PostDto.from(editedPost, null);

        when(userReadService.getUser(userId)).thenReturn(writer);
        when(postWriteService.edit(writer, postId, newContent, null)).thenReturn(editedPostDto);

        PostResponse postResponse = editPostUsecase.execute(userId, postId, postEditRequest);

        assertEquals(newContent, postResponse.content());
        assertEquals(oldImageUrl, postResponse.postImageUrl());
        verify(fileStore, never()).deleteFile(anyString());
    }
}