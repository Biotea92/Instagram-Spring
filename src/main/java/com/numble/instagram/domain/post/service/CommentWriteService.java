package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.repository.CommentRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.exception.badrequest.NotCommentWriterException;
import com.numble.instagram.exception.notfound.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentWriteService {

    private final CommentRepository commentRepository;

    public Comment register(User user, Post post, String content) {
        Comment newComment = Comment.builder()
                .commentWriteUser(user)
                .post(post)
                .content(content)
                .build();
        post.addComment(newComment);
        return commentRepository.save(newComment);
    }

    public Comment edit(User user, Long commentId, String content) {
        Comment comment = getComment(commentId);
        checkCommentWriter(user, comment);
        comment.updateContent(content);
        return comment;
    }

    public void deleteComment(User user, Long commentId) {
        Comment comment = getComment(commentId);
        checkCommentWriter(user, comment);
        commentRepository.delete(comment);
    }

    private static void checkCommentWriter(User user, Comment comment) {
        if (!comment.isCommentWriteUser(user)) {
            throw new NotCommentWriterException();
        }
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }
}
