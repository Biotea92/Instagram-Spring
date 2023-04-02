package com.numble.instagram.domain.follow.service;

import com.numble.instagram.domain.follow.entity.Follow;
import com.numble.instagram.domain.follow.repository.FollowRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.exception.badrequest.AlreadyFollowedUserException;
import com.numble.instagram.exception.badrequest.NotFollowedUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowWriteService {

    private final FollowRepository followRepository;

    public Long follow(User fromUser, User toUser) {
        checkFollowed(fromUser, toUser);
        Follow newFollow = Follow.create(fromUser, toUser);
        return followRepository.save(newFollow).getToUser().getId();
    }

    public Long unfollow(User fromUser, User toUser) {
        Follow followedFollow = followRepository.findByFromUserAndToUser(fromUser, toUser)
                .orElseThrow(NotFollowedUserException::new);
        Long deletedToUserId = toUser.getId();
        followRepository.delete(followedFollow);
        return deletedToUserId;
    }

    private void checkFollowed(User fromUser, User toUser) {
        Optional<Follow> optionalFollow = followRepository.findByFromUserAndToUser(fromUser, toUser);
        if (optionalFollow.isPresent()) {
            throw new AlreadyFollowedUserException();
        }
    }
}
