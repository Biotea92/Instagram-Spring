package com.numble.instagram.domain.follow.repository;

import com.numble.instagram.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
