package com.numble.instagram.presentation.follow;

import com.numble.instagram.application.usecase.follow.CreateFollowUsecase;
import com.numble.instagram.application.usecase.follow.DestroyFollowUsecase;
import com.numble.instagram.dto.common.FollowDto;
import com.numble.instagram.presentation.auth.AuthenticatedUser;
import com.numble.instagram.presentation.auth.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {

    private final CreateFollowUsecase createFollowUsecase;
    private final DestroyFollowUsecase destroyFollowUsecase;

    @Login
    @PostMapping("/{toUserId}")
    public FollowDto follow(@AuthenticatedUser Long fromUserId,
                            @PathVariable Long toUserId) {
        Long savedToUserId = createFollowUsecase.execute(fromUserId, toUserId);
        return FollowDto.from(savedToUserId);
    }

    @Login
    @PostMapping("/unfollow/{toUserId}")
    public FollowDto unfollow(@AuthenticatedUser Long fromUserId,
                            @PathVariable Long toUserId) {
        Long deletedToUserId = destroyFollowUsecase.execute(fromUserId, toUserId);
        return FollowDto.from(deletedToUserId);
    }
}
