package com.numble.instagram.application.usecase.follow;

import com.numble.instagram.domain.follow.service.FollowWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateFollowUserUsecase {

    private final UserReadService userReadService;
    private final FollowWriteService followWriteService;

    public Long execute(Long fromUserId, Long toUserId) {
        User fromUser = userReadService.getUser(fromUserId);
        User toUser = userReadService.getUser(toUserId);
        return followWriteService.follow(fromUser, toUser).getToUser().getId();
    }
}
