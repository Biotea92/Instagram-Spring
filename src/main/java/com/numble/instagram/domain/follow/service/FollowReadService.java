package com.numble.instagram.domain.follow.service;

import com.numble.instagram.domain.follow.entity.Follow;
import com.numble.instagram.domain.follow.repository.FollowRepository;
import com.numble.instagram.domain.user.entity.User;
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

    public List<Follow> getFollowersFollow(User toUser) {
        return followRepository.findByToUser(toUser.getId());
    }

    public List<Follow> getFollowingsFollow(User fromUser) {
        return followRepository.findByFromUser(fromUser.getId());
    }
}
