package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.repository.CommentRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.exception.badrequest.NotCommentWriterException;
import com.numble.instagram.exception.notfound.CommentNotFoundException;
import com.numble.instagram.util.fixture.post.CommentFixture;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentWriteServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentWriteService commentWriteService;

    @Test
    @DisplayName("댓글은 저장되어야한다.")
    void register() {
        User commentWriter = UserFixture.create("commentWriter");
        Post post = PostFixture.create("post-content");
        String content = "댓글 입니다.";
        Comment comment = CommentFixture.create(commentWriter, post, content);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = commentWriteService.register(commentWriter, post, content);

        assertEquals(commentWriter, result.getCommentWriteUser());
        assertEquals(content, result.getContent());
        assertEquals(post, result.getPost());
    }

    @Test
    @DisplayName("댓글은 수정되어야 한다.")
    void edit() {
        User user = UserFixture.create("user");
        Post post = PostFixture.create("post-content");
        Comment comment = CommentFixture.create(user, post, "old comment content");

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        String newContent = "new_comment_content";
        Comment editedComment = commentWriteService.edit(user, comment.getId(), newContent);
        assertEquals(newContent, editedComment.getContent());
    }

    @Test
    @DisplayName("댓글 작성자가 아니면 NotCommentWriterException이 발생한다.")
    void wrongWriter() {
        User user = UserFixture.create("user");
        Post post = PostFixture.create("post-content");
        Comment comment = CommentFixture.create(user, post, "old comment content");

        User wrongWriter = UserFixture.create("wrongWriter");
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        assertThrows(
                NotCommentWriterException.class,
                () -> commentWriteService.edit(wrongWriter, comment.getId(), "new_comment_content")
        );
    }

    @Test
    @DisplayName("존재하지 않는 댓글을 수정하려고하면 NonExistingComment이 발생한다.")
    public void testEditNonExistingComment() {
        User user = UserFixture.create("user");
        Post post = PostFixture.create("post-content");
        Comment comment = CommentFixture.create(user, post);
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () ->
            commentWriteService.edit(user, comment.getId(), "test_comment_content")
        );
    }
}