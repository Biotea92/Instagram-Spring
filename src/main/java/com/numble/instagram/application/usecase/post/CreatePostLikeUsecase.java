package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.entity.PostLike;
import com.numble.instagram.domain.post.service.PostLikeWriteService;
import com.numble.instagram.domain.post.service.PostReadService;
import com.numble.instagram.domain.post.service.PostWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.response.post.PostLikeResponse;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePostLikeUsecase {

    private final UserReadService userReadService;
    private final PostReadService postReadService;
    private final PostLikeWriteService postLikeWriteService;
    private final PostWriteService postWriteService;

    public PostLikeResponse execute(Long userId, Long postId) {
        User user = userReadService.getUser(userId);
        Post post = postReadService.getPost(postId);
        PostLike newPostLike = postLikeWriteService.like(user, post);

        // TODO 어떻게 바꿔야할지 고민해 볼 것
        try {
            postWriteService.upLike(post);
        } catch (OptimisticLockException ex) {
            throw new RuntimeException("낙관적 락 동시성 문제 발생");
        }
        return PostLikeResponse.from(newPostLike);
    }
}
