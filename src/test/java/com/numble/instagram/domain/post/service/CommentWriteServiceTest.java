package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.repository.CommentRepository;
import com.numble.instagram.domain.user.entity.User;
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
}