package com.numble.instagram.util.fixture.user;

import com.numble.instagram.dto.response.user.UserResponse;

import java.time.LocalDateTime;

public class UserResponseFixture {

    private static Long id = 0L;

    public static UserResponse create(String nickname) {
        return new UserResponse(id++, nickname, "https://profile.com", LocalDateTime.now());
    }
}
