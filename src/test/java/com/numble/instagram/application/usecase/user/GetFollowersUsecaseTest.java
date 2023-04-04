package com.numble.instagram.application.usecase.user;

import com.numble.instagram.domain.follow.entity.Follow;
import com.numble.instagram.domain.follow.service.FollowReadService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.response.user.UserResponse;
import com.numble.instagram.util.fixture.follow.FollowFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFollowersUsecaseTest {

    @Mock
    private UserReadService userReadService;

    @Mock
    private FollowReadService followReadService;

    @InjectMocks
    private GetFollowersUsecase getFollowersUsecase;

    @Test
    @DisplayName("결과가 유저의 닉네임 순으로 sort 되었는지 확인한다.")
    void testExecute() {
        User user = UserFixture.create("user1");
        User follower1 = UserFixture.create("follower1");
        User follower2 = UserFixture.create("follower2");
        Follow follow1 = FollowFixture.create(follower1, user);
        Follow follow2 = FollowFixture.create(follower2, user);
        List<Follow> followList = List.of(follow1, follow2);
        when(userReadService.getUser(user.getId())).thenReturn(user);
        when(followReadService.getFollowersFollow(user)).thenReturn(followList);

        List<UserResponse> result = getFollowersUsecase.execute(user.getId());

        assertEquals("follower1", result.get(0).nickname());
    }
}