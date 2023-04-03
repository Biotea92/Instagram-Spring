package com.numble.instagram.domain.post.repository;

import com.numble.instagram.domain.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
