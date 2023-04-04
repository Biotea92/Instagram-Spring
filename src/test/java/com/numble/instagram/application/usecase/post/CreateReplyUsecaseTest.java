package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Reply;
import com.numble.instagram.domain.post.service.CommentReadService;
import com.numble.instagram.domain.post.service.ReplyWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.request.post.ReplyRequest;
import com.numble.instagram.dto.response.post.ReplyResponse;
import com.numble.instagram.util.fixture.post.CommentFixture;
import com.numble.instagram.util.fixture.post.ReplyFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateReplyUsecaseTest {

    @Mock
    private UserReadService userReadService;
    @Mock
    private CommentReadService commentReadService;
    @Mock
    private ReplyWriteService replyWriteService;
    @InjectMocks
    private CreateReplyUsecase createReplyUsecase;

    @Test
    @DisplayName("답글은 생성되어야 한다.")
    void executeShouldCreateReply() {
        Long userId = 1L;
        Long commentId = 2L;
        String content = "Test reply";
        User user = UserFixture.create("user");
        Comment comment = CommentFixture.create("comment-content");

        ReplyRequest replyRequest = new ReplyRequest(content);
        Reply reply = ReplyFixture.create(user, comment, content);

        when(userReadService.getUser(userId)).thenReturn(user);
        when(commentReadService.getComment(commentId)).thenReturn(comment);
        when(replyWriteService.register(user, comment, content)).thenReturn(reply);
        ReplyResponse expectedResponse = ReplyResponse.from(reply);

        ReplyResponse actualResponse = createReplyUsecase.execute(userId, commentId, replyRequest);

        assertEquals(expectedResponse, actualResponse);
    }
}