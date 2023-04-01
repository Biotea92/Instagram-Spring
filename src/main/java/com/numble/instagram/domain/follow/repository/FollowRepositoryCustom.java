package com.numble.instagram.domain.follow.repository;

public interface FollowRepositoryCustom {

    Long getFollowerCount(Long userId);
    Long getFollowingCount(Long userId);
}
