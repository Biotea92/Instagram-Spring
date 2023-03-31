package com.numble.instagram.domain.follow.service;

import com.numble.instagram.domain.follow.entity.Follow;
import com.numble.instagram.domain.follow.repository.FollowRepository;
import com.numble.instagram.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowWriteService {

    private final FollowRepository followRepository;

    public Follow follow(User fromUser, User toUser) {
        Follow follow = Follow.create(fromUser, toUser);
        return followRepository.save(follow);
    }
}
