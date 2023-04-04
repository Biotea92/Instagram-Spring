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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFollowingsUsecaseTest {

    @Mock
    private UserReadService userReadService;

    @Mock
    private FollowReadService followReadService;

    @InjectMocks
    private GetFollowingsUsecase getFollowingsUsecase;

    @Test
    @DisplayName("결과가 유저의 닉네임 순으로 sort 되었는지 확인한다.")
    void testExecute() {
        User user = UserFixture.create("user1");
        User following1 = UserFixture.create("following1");
        User following2 = UserFixture.create("following2");
        Follow followingFollow1 = FollowFixture.create(user, following1);
        Follow followingFollow2 = FollowFixture.create(user, following2);
        List<Follow> followList = List.of(followingFollow1, followingFollow2);
        when(userReadService.getUser(user.getId())).thenReturn(user);
        when(followReadService.getFollowingsFollow(user.getId())).thenReturn(followList);

        List<UserResponse> result = getFollowingsUsecase.execute(user.getId());

        assertEquals("following1", result.get(0).nickname());
    }
}