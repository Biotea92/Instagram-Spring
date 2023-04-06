package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.service.PostLikeWriteService;
import com.numble.instagram.domain.post.service.PostReadService;
import com.numble.instagram.domain.post.service.PostWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.response.post.PostLikeResponse;
import com.numble.instagram.util.fixture.post.PostFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePostLikeUsecaseTest {

    @InjectMocks
    private CreatePostLikeUsecase createPostLikeUsecase;
    @Mock
    private UserReadService userReadService;
    @Mock
    private PostReadService postReadService;
    @Mock
    private PostLikeWriteService postLikeWriteService;
    @Mock
    private PostWriteService postWriteService;

    @Test
    @DisplayName("createPostLikeUsecase는 실행되어야한다.")
    void execute() {
        Long userId = 1L;
        Long postId = 2L;
        User user = UserFixture.create("1L-user");
        Post post = PostFixture.create("1L-post");
        when(userReadService.getUser(userId)).thenReturn(user);
        when(postReadService.getPost(postId)).thenReturn(post);

        PostLikeResponse postLikeResponse = createPostLikeUsecase.execute(userId, postId);

        assertEquals(postId, postLikeResponse.postId());
        verify(postLikeWriteService, times(1)).like(user, post);
        verify(postWriteService, times(1)).upLikeCount(post);
    }

    @Test
    @DisplayName("낙관적 락이 발생했을 때")
    void execute_optimisticLockException() {
        Long userId = 1L;
        Long postId = 2L;
        User user = UserFixture.create("1L-user");
        Post post = PostFixture.create("1L-post");
        when(userReadService.getUser(userId)).thenReturn(user);
        when(postReadService.getPost(postId)).thenReturn(post);
        doThrow(OptimisticLockException.class).when(postWriteService).upLikeCount(any(Post.class));

        assertThrows(RuntimeException.class, () -> createPostLikeUsecase.execute(userId, postId));

        verify(postLikeWriteService, times(1)).like(user, post);
        verify(postWriteService, times(1)).upLikeCount(post);
    }
}