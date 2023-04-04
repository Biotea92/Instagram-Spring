package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.repository.CommentRepository;
import com.numble.instagram.domain.user.entity.User;
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
        return commentRepository.save(newComment);
    }
}
