package com.numble.instagram.domain.post.repository;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.entity.PostLike;
import com.numble.instagram.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByUserAndPost(User user, Post post);
}
