package com.numble.instagram.presentation.post;

import com.numble.instagram.application.usecase.post.CreatePostUsecase;
import com.numble.instagram.dto.request.post.PostCreateRequest;
import com.numble.instagram.dto.response.post.PostResponse;
import com.numble.instagram.presentation.auth.AuthenticatedUser;
import com.numble.instagram.presentation.auth.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final CreatePostUsecase createPostUsecase;

    @Login
    @PostMapping
    public PostResponse register(@AuthenticatedUser Long userId,
                                 @Validated PostCreateRequest postCreateRequest) {
        return createPostUsecase.execute(userId, postCreateRequest);
    }
}
