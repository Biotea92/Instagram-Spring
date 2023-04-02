package com.numble.instagram.application.usecase.user;

import com.numble.instagram.domain.follow.service.FollowReadService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.response.user.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetUserProfileUsecase {

    private final UserReadService userReadService;
    private final FollowReadService followReadService;

    public UserDetailResponse execute(Long userId) {
        User user = userReadService.getUser(userId);
        Long followerCount = followReadService.getFollowerCount(userId);
        Long followingCount = followReadService.getFollowingCount(userId);

        return UserDetailResponse.from(user, followerCount, followingCount);
    }
}
