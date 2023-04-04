package com.numble.instagram.domain.follow.service;

import com.numble.instagram.domain.follow.entity.Follow;
import com.numble.instagram.domain.follow.repository.FollowRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.repository.UserRepository;
import com.numble.instagram.util.fixture.follow.FollowFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class FollowReadServiceTest {

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowReadService followReadService;

    @Test
    @DisplayName("팔로우 한 사람의 수를 가져올 수 있다.")
    void getFollowingCount() {
        User user = UserFixture.create("user");
        userRepository.save(user);

        List<Follow> followings = new LinkedList<>();
        LongStream.range(1, 20).forEach(i -> {
            User FollowingUser = UserFixture.create("followingUser");
            userRepository.saveAndFlush(FollowingUser);
            followings.add(FollowFixture.create(user, FollowingUser));
        });
        followRepository.saveAllAndFlush(followings);

        Long followingCount = followReadService.getFollowingCount(user.getId());

        assertEquals(19L, followingCount);
    }

    @Test
    @DisplayName("팔로워의 수를 가져올 수 있다.")
    void getFollowerCount() {
        User user = UserFixture.create("user");
        userRepository.save(user);

        List<Follow> followers = new LinkedList<>();
        LongStream.range(1, 20).forEach(i -> {
            User followerUser = UserFixture.create("followerUser");
            userRepository.saveAndFlush(followerUser);
            followers.add(FollowFixture.create(followerUser, user));
        });
        followRepository.saveAllAndFlush(followers);

        Long followingCount = followReadService.getFollowerCount(user.getId());

        assertEquals(19L, followingCount);
    }

    @Test
    @DisplayName("팔로워 팔로우들을 가져온다.")
    void getFollowersFollow() {
        User user = UserFixture.create("user");
        userRepository.save(user);

        List<Follow> followers = new LinkedList<>();
        LongStream.range(1, 20).forEach(i -> {
            User followerUser = UserFixture.create("followerUser");
            userRepository.saveAndFlush(followerUser);
            followers.add(FollowFixture.create(followerUser, user));
        });
        followRepository.saveAllAndFlush(followers);

        List<Follow> followersFollow = followReadService.getFollowersFollow(user.getId());

        assertEquals(19, followersFollow.size());
    }

    @Test
    @DisplayName("팔로잉 팔로우들을 가져온다")
    void getFollowingsFollow() {
        User user = UserFixture.create("user");
        userRepository.save(user);

        List<Follow> followings = new LinkedList<>();
        LongStream.range(1, 20).forEach(i -> {
            User FollowingUser = UserFixture.create("followingUser");
            userRepository.saveAndFlush(FollowingUser);
            followings.add(FollowFixture.create(user, FollowingUser));
        });
        followRepository.saveAllAndFlush(followings);

        List<Follow> followingsFollow = followReadService.getFollowingsFollow(user.getId());

        assertEquals(19, followingsFollow.size());
    }
}