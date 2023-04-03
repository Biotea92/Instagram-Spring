package com.numble.instagram.domain.post.repository;

import com.numble.instagram.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
