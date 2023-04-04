package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.service.CommentWriteService;
import com.numble.instagram.domain.post.service.PostReadService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.request.post.CommentCreateRequest;
import com.numble.instagram.dto.response.post.CommentResponse;
import com.numble.instagram.util.fixture.post.CommentFixture;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCommentUsecaseTest {

    @Mock
    private UserReadService userReadService;
    @Mock
    private PostReadService postReadService;
    @Mock
    private CommentWriteService commentWriteService;
    @InjectMocks
    private CreateCommentUsecase createCommentUsecase;

    @Test
    @DisplayName("댓글은 생성되어야 한다.")
    void createCommentExecute() {
        Long userId = 1L;
        Long postId = 2L;
        CommentCreateRequest request = new CommentCreateRequest("test comment");
        User user = UserFixture.create(userId, "user");
        Post post = PostFixture.create("post-content");
        Comment comment = CommentFixture.create(user, post, "test comment");
        when(userReadService.getUser(userId)).thenReturn(user);
        when(postReadService.getPost(postId)).thenReturn(post);
        when(commentWriteService.register(any(), any(), any())).thenReturn(comment);

        CommentResponse result = createCommentUsecase.execute(userId, postId, request);

        verify(userReadService, times(1)).getUser(userId);
        verify(postReadService, times(1)).getPost(postId);
        verify(commentWriteService, times(1)).register(user, post, request.content());
        assertEquals("test comment", result.content());
        assertEquals(post.getId(), result.id());
    }
}