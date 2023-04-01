package com.numble.instagram.dto.response.user;

import com.numble.instagram.domain.user.entity.User;

public record UserDetailResponse(String nickname, String profileImageUrl, Long follower, Long following) {

    public static UserDetailResponse from(User user, Long followerCount, Long followingCount) {
        return new UserDetailResponse(user.getNickname(), user.getProfileImageUrl(), followerCount, followingCount);
    }
}
