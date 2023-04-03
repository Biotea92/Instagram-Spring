package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.follow.entity.Follow;
import com.numble.instagram.domain.follow.service.FollowReadService;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.service.FeedWriteService;
import com.numble.instagram.domain.post.service.PostWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.request.post.PostCreateRequest;
import com.numble.instagram.dto.response.post.PostResponse;
import com.numble.instagram.support.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreatePostUsecase {

    private final UserReadService userReadService;
    private final PostWriteService postWriteService;
    private final FollowReadService followReadService;
    private final FeedWriteService feedWriteService;
    private final FileStore fileStore;

    public PostResponse execute(Long userId, PostCreateRequest postCreateRequest) {
        User writerUser = userReadService.getUser(userId);
        String postImageUrl = fileStore.uploadImage(postCreateRequest.postImageFile());

        Post newPost = postWriteService.register(writerUser, postCreateRequest.content(), postImageUrl);
        List<User> followers = followReadService.getFollowersFollow(writerUser).stream()
                .map(Follow::getFromUser)
                .toList();

        feedWriteService.deliveryToFeed(newPost, followers);

        return PostResponse.from(newPost);
    }
}
