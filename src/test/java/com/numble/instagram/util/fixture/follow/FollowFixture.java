package com.numble.instagram.util.fixture.follow;

import com.numble.instagram.domain.follow.entity.Follow;
import com.numble.instagram.domain.user.entity.User;

public class FollowFixture {

    public static Follow create(User fromUser, User toUser) {
        return new Follow(fromUser, toUser);
    }
}
