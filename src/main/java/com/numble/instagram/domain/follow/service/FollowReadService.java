package com.numble.instagram.domain.follow.service;

import com.numble.instagram.domain.follow.entity.Follow;
import com.numble.instagram.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Follow> getFollowersFollow(Long toUserId) {
        return followRepository.findByToUserIdWithFromUser(toUserId);
    }

    public List<Follow> getFollowingsFollow(Long fromUserId) {
        return followRepository.findByFromUserIdWithToUser(fromUserId);
    }
}
