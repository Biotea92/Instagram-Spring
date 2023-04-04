package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.repository.CommentRepository;
import com.numble.instagram.exception.notfound.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentReadService {

    private final CommentRepository commentRepository;

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }
}
