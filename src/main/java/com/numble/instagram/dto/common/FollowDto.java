package com.numble.instagram.dto.common;

public record FollowDto(Long userId) {

    public static FollowDto from(Long userId) {
        return new FollowDto(userId);
    }
}
