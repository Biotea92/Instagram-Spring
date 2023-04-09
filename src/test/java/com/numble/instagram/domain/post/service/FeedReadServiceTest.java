package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Feed;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.entity.Reply;
import com.numble.instagram.domain.post.repository.CommentRepository;
import com.numble.instagram.domain.post.repository.FeedRepository;
import com.numble.instagram.domain.post.repository.PostRepository;
import com.numble.instagram.domain.post.repository.ReplyRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.repository.UserRepository;
import com.numble.instagram.support.paging.CursorRequest;
import com.numble.instagram.util.fixture.post.CommentFixture;
import com.numble.instagram.util.fixture.post.FeedFixture;
import com.numble.instagram.util.fixture.post.PostFixture;
import com.numble.instagram.util.fixture.post.ReplyFixture;
import com.numble.instagram.util.fixture.user.UserFixture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FeedReadServiceTest {

    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private FeedReadService feedReadService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("feed는 조회되어야 한다.")
    @Transactional
    void getFeeds() {
        User user = UserFixture.create("user");
        userRepository.save(user);

        List<Post> posts = PostFixture.createPosts(user);
        postRepository.saveAll(posts);

        User follower = UserFixture.create("follower");
        userRepository.save(follower);
        List<Feed> feeds = FeedFixture.createFeeds(follower, posts);
        feedRepository.saveAll(feeds);

        List<Comment> comments = CommentFixture.createComments(user, posts);
        commentRepository.saveAll(comments);

        List<Reply> replies = ReplyFixture.createReplies(user, comments);
        replyRepository.saveAll(replies);

        em.clear();
        em.flush();

        CursorRequest cursorRequest = new CursorRequest(null, 10);
        List<Feed> result = feedReadService.getFeeds(follower.getId(), cursorRequest);

        assertEquals(10, result.size());
        for (Feed feed : result) {
            System.out.println("feed.getPost().getContent() = " + feed.getPost().getContent());
        }
    }
}