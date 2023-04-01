package com.numble.instagram.domain.follow.service;

import com.numble.instagram.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowReadService {

    private final FollowRepository followRepository;

    public Long getFollowerCount(Long userId) {
        return followRepository.getFollowerCount(userId);
    }

    public Long getFollowingCount(Long userId) {
        return followRepository.getFollowingCount(userId);
    }
}
