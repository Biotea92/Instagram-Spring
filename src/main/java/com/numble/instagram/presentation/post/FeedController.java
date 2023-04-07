package com.numble.instagram.presentation.post;

import com.numble.instagram.application.usecase.post.GetFeedPostsUsecase;
import com.numble.instagram.presentation.auth.AuthenticatedUser;
import com.numble.instagram.presentation.auth.Login;
import com.numble.instagram.support.paging.CursorRequest;
import com.numble.instagram.support.paging.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class FeedController {

    private final GetFeedPostsUsecase getFeedPostsUsecase;

    @Login
    @GetMapping
    public PageCursor<?> getFeed(@AuthenticatedUser Long userId,
                                 CursorRequest cursorRequest) {
        return getFeedPostsUsecase.execute(userId, cursorRequest);
    }
}
