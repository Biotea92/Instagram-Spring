package com.numble.instagram.domain.post.repository;

import com.numble.instagram.domain.post.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {
}
