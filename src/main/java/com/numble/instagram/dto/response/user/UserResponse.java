package com.numble.instagram.dto.response.user;

import com.numble.instagram.domain.user.entity.User;

import java.time.LocalDateTime;

public record UserResponse(Long id, String nickname, String profileImageUrl, LocalDateTime joinedAt) {

    public static UserResponse create(User user) {
        return new UserResponse(user.getId(), user.getNickname(), user.getProfileImageUrl(), user.getJoinedAt());
    }
}
