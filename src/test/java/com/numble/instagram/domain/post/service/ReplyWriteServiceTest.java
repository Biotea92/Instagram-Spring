package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Reply;
import com.numble.instagram.domain.post.repository.ReplyRepository;
import com.numble.instagram.domain.user.entity.User;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReplyWriteServiceTest {

    @Mock
    private ReplyRepository replyRepository;
    @InjectMocks
    private ReplyWriteService replyWriteService;

    @Test
    @DisplayName("답글은 저장되어야한다.")
    void register() {
        User replyWriter = UserFixture.create("replyWriter");
        Comment comment = CommentFixture.create("comment-content");
        String content = "답글 입니다.";

        Reply reply = ReplyFixture.create(replyWriter, comment, content);
        when(replyRepository.save(any(Reply.class))).thenReturn(reply);

        Reply result = replyWriteService.register(replyWriter, comment, content);

        assertEquals(replyWriter, result.getReplyWriteUser());
        assertEquals(content, result.getContent());
        assertEquals(comment, result.getComment());
    }
}