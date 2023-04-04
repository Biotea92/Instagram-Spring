package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.follow.entity.Follow;
import com.numble.instagram.domain.follow.service.FollowReadService;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.service.FeedWriteService;
import com.numble.instagram.domain.post.service.PostWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.request.post.PostCreateRequest;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePostUsecaseTest {

    @Mock
    private UserReadService userReadService;
    @Mock
    private PostWriteService postWriteService;
    @Mock
    private FollowReadService followReadService;
    @Mock
    private FeedWriteService feedWriteService;
    @Mock
    private FileStore fileStore;
    @InjectMocks
    private CreatePostUsecase createPostUsecase;

    @Test
    @DisplayName("글을 생성하는 유즈케이스 테스트")
    void execute_createPost() {
        Long userId = 1L;
        User writerUser = UserFixture.create(userId, "writerUser");

        MultipartFile postImageFile = new MockMultipartFile(
                "test.jpg", "test.jpg", "image/jpeg", "test.jpg".getBytes());
        PostCreateRequest postCreateRequest = new PostCreateRequest("test post", postImageFile);

        String postImageUrl = "https://example.com/test.jpg";

        Post newPost = PostFixture.create(postImageUrl, postCreateRequest.content(), writerUser);

        User follower1 = UserFixture.create(2L, "follower1");
        User follower2 = UserFixture.create(3L, "follower2");
        List<User> followers = List.of(follower1, follower2);

        when(userReadService.getUser(userId)).thenReturn(writerUser);
        when(fileStore.uploadImage(postCreateRequest.postImageFile())).thenReturn(postImageUrl);
        when(postWriteService.register(writerUser, postCreateRequest.content(), postImageUrl)).thenReturn(newPost);
        when(followReadService.getFollowersFollow(writerUser.getId())).thenReturn(
                List.of(Follow.create(follower1, writerUser), Follow.create(follower2, writerUser))
        );

        PostResponse postResponse = createPostUsecase.execute(userId, postCreateRequest);

        verify(postWriteService).register(writerUser, postCreateRequest.content(), postImageUrl);
        verify(followReadService).getFollowersFollow(writerUser.getId());
        verify(feedWriteService).deliveryToFeed(newPost, followers);
        verifyNoMoreInteractions(postWriteService, followReadService, feedWriteService);

        PostResponse expectedResponse = PostResponse.from(newPost);
        assertEquals(expectedResponse, postResponse);
    }
}