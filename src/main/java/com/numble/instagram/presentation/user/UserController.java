package com.numble.instagram.presentation.user;

import com.numble.instagram.domain.user.service.UserWriteService;
import com.numble.instagram.dto.request.user.UserEditRequest;
import com.numble.instagram.dto.request.user.UserJoinRequest;
import com.numble.instagram.dto.response.user.UserResponse;
import com.numble.instagram.presentation.auth.AuthenticatedUser;
import com.numble.instagram.presentation.auth.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserWriteService userWriteService;

    @PostMapping
    public UserResponse join(@Validated UserJoinRequest userJoinRequest) {
        return userWriteService.join(userJoinRequest);
    }

    @Login
    @PostMapping("/edit")
    public UserResponse edit(@AuthenticatedUser Long userId,
                             @Validated @RequestBody UserEditRequest userEditRequest) {
        return userWriteService.edit(userId, userEditRequest.nickname(), userEditRequest.profileImageFile());
    }
}
