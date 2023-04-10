package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.service.FeedWriteService;
import com.numble.instagram.domain.post.service.PostLikeWriteService;
import com.numble.instagram.domain.post.service.PostReadService;
import com.numble.instagram.domain.post.service.PostWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DestroyPostUsecase {

    private final UserReadService userReadService;
    private final PostReadService postReadService;
    private final PostLikeWriteService postLikeWriteService;
    private final FeedWriteService feedWriteService;
    private final PostWriteService postWriteService;

    public void execute(Long userId, Long postId) {
        User user = userReadService.getUser(userId);
        Post post = postReadService.getPost(postId);
        if (post.isWriter(user)) {
            postLikeWriteService.deletePostLike(post);
            feedWriteService.deleteFeed(post);
        }
        postWriteService.deletePost(user, post);
    }
}
