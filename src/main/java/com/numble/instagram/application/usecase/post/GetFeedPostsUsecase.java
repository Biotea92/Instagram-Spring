package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.post.entity.Feed;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.service.FeedReadService;
import com.numble.instagram.domain.post.service.PostLikeReadService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.response.post.PostDetailResponse;
import com.numble.instagram.support.paging.CursorRequest;
import com.numble.instagram.support.paging.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetFeedPostsUsecase {

    private final UserReadService userReadService;
    private final FeedReadService feedReadService;
    private final PostLikeReadService postLikeReadService;

    public PageCursor<?> execute(Long userId, CursorRequest cursorRequest) {
        User user = userReadService.getUser(userId);
        List<Feed> feeds = feedReadService.getFeeds(user.getId(), cursorRequest);

        Long nextKey = feeds.stream()
                .mapToLong(Feed::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);

        List<Post> posts = feeds.stream()
                .map(Feed::getPost)
                .toList();

        Iterator<Boolean> isPostLikedIterator = postLikeReadService.getPostLike(user, posts).iterator();

        List<PostDetailResponse> postDetailResponses = posts.stream()
                .map(post -> PostDetailResponse.from(post, isPostLikedIterator.next()))
                .toList();

        return PageCursor.from(cursorRequest.next(nextKey), postDetailResponses);
    }
}
