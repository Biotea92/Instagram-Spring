package com.numble.instagram.presentation.user;

import com.numble.instagram.domain.user.service.UserWriteService;
import com.numble.instagram.dto.request.user.UserJoinRequest;
import com.numble.instagram.dto.response.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserWriteService userWriteService;

    @PostMapping
    public UserResponse join(@Validated UserJoinRequest userJoinRequest) {
        return userWriteService.join(userJoinRequest);
    }
}
