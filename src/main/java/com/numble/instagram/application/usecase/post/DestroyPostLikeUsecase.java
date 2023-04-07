package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.service.PostLikeWriteService;
import com.numble.instagram.domain.post.service.PostReadService;
import com.numble.instagram.domain.post.service.PostWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.response.post.PostLikeResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.StaleStateException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DestroyPostLikeUsecase {

    private final UserReadService userReadService;
    private final PostReadService postReadService;
    private final PostLikeWriteService postLikeWriteService;
    private final PostWriteService postWriteService;

    public PostLikeResponse execute(Long userId, Long postId) {
        User user = userReadService.getUser(userId);
        Post post = postReadService.getPost(postId);
        postLikeWriteService.dislike(user, post);

        // TODO 어떻게 바꿔야할지 고민해 볼 것
        try {
            postWriteService.downLikeCount(post);
        } catch (ObjectOptimisticLockingFailureException | StaleStateException e) {
            // 뭔가를 해야만 한다.
        }
        return PostLikeResponse.from(postId);
    }
}
