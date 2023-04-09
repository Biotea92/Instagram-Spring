package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Reply;
import com.numble.instagram.domain.post.repository.ReplyRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.exception.badrequest.NotReplyWriterException;
import com.numble.instagram.exception.notfound.ReplyNotFoundException;
import com.numble.instagram.util.fixture.post.CommentFixture;
import com.numble.instagram.util.fixture.post.ReplyFixture;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    @Test
    @DisplayName("답글은 수정되어야 한다.")
    void edit() {
        User user = UserFixture.create("user");
        Comment comment = CommentFixture.create("comment-content");

        Reply reply = ReplyFixture.create(user, comment, "old reply content");
        when(replyRepository.findById(reply.getId())).thenReturn(Optional.of(reply));
        String newContent = "new_reply_content";

        Reply result = replyWriteService.edit(user, reply.getId(), newContent);
        assertEquals(newContent, result.getContent());
    }

    @Test
    @DisplayName("답글 작성자가 아니면 NotReplyWriterException이 발생한다.")
    void wrongWriter() {
        User user = UserFixture.create("user");
        Comment comment = CommentFixture.create("comment-content");
        Reply reply = ReplyFixture.create(user, comment);
        User wrongWriter = UserFixture.create("wrongWriter");
        when(replyRepository.findById(eq(comment.getId()))).thenReturn(Optional.of(reply));

        assertThrows(
                NotReplyWriterException.class,
                () -> replyWriteService.edit(wrongWriter, reply.getId(), "new_reply_content")
        );
    }

    @Test
    @DisplayName("존재하지 않는 답글을 수정하려고하면 NonExistingReply이 발생한다.")
    void testEditNonExistingComment() {
        User user = UserFixture.create("user");
        Comment comment = CommentFixture.create("comment-content");
        Reply reply = ReplyFixture.create(user, comment, "old reply content");
        when(replyRepository.findById(eq(user.getId()))).thenReturn(Optional.empty());
        assertThrows(ReplyNotFoundException.class, () ->
                replyWriteService.edit(user, reply.getId(), "test_comment_content")
        );
    }
}