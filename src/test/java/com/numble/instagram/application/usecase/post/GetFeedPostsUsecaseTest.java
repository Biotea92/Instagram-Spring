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
import com.numble.instagram.util.fixture.post.FeedFixture;
import com.numble.instagram.util.fixture.post.PostFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFeedPostsUsecaseTest {

    @InjectMocks
    private GetFeedPostsUsecase getFeedPostsUsecase;

    @Mock
    private UserReadService userReadService;

    @Mock
    private FeedReadService feedReadService;

    @Mock
    private PostLikeReadService postLikeReadService;

    @Test
    @DisplayName("피드는 조회되어야한다.")
    void execute() {
        User user = UserFixture.create("user");
        Post post1 = PostFixture.create( "post-content1");
        Post post2 = PostFixture.create("post-content2");
        List<Feed> feeds = FeedFixture.createFeedsWithIds(user, List.of(post1, post2), List.of(1L, 2L));
        CursorRequest cursorRequest = new CursorRequest(null, 10);

        when(userReadService.getUser(user.getId())).thenReturn(user);
        when(feedReadService.getFeeds(user.getId(), cursorRequest)).thenReturn(feeds);
        when(postLikeReadService.getPostLikes(user, List.of(feeds.get(0).getPost(), feeds.get(1).getPost()))).thenReturn(List.of(true, false));

        PageCursor<?> result = getFeedPostsUsecase.execute(user.getId(), cursorRequest);

        assertThat(result.posts()).hasSize(2);
        assertThat(result.nextCursorRequest().key()).isEqualTo(1L);
        PostDetailResponse postDetailResponse1 = (PostDetailResponse) result.posts().get(0);
        PostDetailResponse postDetailResponse2 = (PostDetailResponse) result.posts().get(1);
        assertThat(postDetailResponse1.postId()).isEqualTo(feeds.get(0).getPost().getId());
        assertThat(postDetailResponse1.isPostLiked()).isTrue();
        assertThat(postDetailResponse2.postId()).isEqualTo(feeds.get(1).getPost().getId());
        assertThat(postDetailResponse2.isPostLiked()).isFalse();
    }
}