package com.numble.instagram.domain.follow.service;

import com.numble.instagram.domain.follow.entity.Follow;
import com.numble.instagram.domain.follow.repository.FollowRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.exception.badrequest.AlreadyFollowedUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowWriteService {

    private final FollowRepository followRepository;

    public Follow follow(User fromUser, User toUser) {
        checkFollowed(fromUser, toUser);
        Follow follow = Follow.create(fromUser, toUser);
        return followRepository.save(follow);
    }

    private void checkFollowed(User fromUser, User toUser) {
        Optional<Follow> optionalFollow = followRepository.findByFromUserAndToUser(fromUser, toUser);
        followRepository.findByFromUserAndToUser(fromUser, toUser);
        if (optionalFollow.isPresent()) {
            throw new AlreadyFollowedUserException();
        }
    }
}
