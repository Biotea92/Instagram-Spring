package com.numble.instagram.presentation.user;

import com.numble.instagram.application.usecase.user.GetFollowersUsecase;
import com.numble.instagram.application.usecase.user.GetFollowingsUsecase;
import com.numble.instagram.application.usecase.user.GetUserProfileUsecase;
import com.numble.instagram.domain.user.service.UserWriteService;
import com.numble.instagram.dto.request.user.UserEditRequest;
import com.numble.instagram.dto.request.user.UserJoinRequest;
import com.numble.instagram.dto.response.user.UserDetailResponse;
import com.numble.instagram.dto.response.user.UserResponse;
import com.numble.instagram.presentation.auth.AuthenticatedUser;
import com.numble.instagram.presentation.auth.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserWriteService userWriteService;
    private final GetUserProfileUsecase getUserProfileUsecase;
    private final GetFollowersUsecase getFollowersUsecase;
    private final GetFollowingsUsecase getFollowingsUsecase;

    @PostMapping
    public UserResponse join(@Validated UserJoinRequest userJoinRequest) {
        return userWriteService.join(userJoinRequest);
    }

    @Login
    @PostMapping("/edit")
    public UserResponse edit(@AuthenticatedUser Long userId,
                             @Validated UserEditRequest userEditRequest) {
        return userWriteService.edit(userId, userEditRequest.nickname(), userEditRequest.profileImageFile());
    }

    @GetMapping("/{userId}")
    public UserDetailResponse get(@PathVariable Long userId) {
        return getUserProfileUsecase.execute(userId);
    }

    @GetMapping("{userId}/followers")
    public List<UserResponse> getFollowers(@PathVariable Long userId) {
        return getFollowersUsecase.execute(userId);
    }

    @GetMapping("{userId}/followings")
    public List<UserResponse> getFollowings(@PathVariable Long userId) {
        return getFollowingsUsecase.execute(userId);
    }
}
