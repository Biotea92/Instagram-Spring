package com.numble.instagram.util.fixture.post;

import com.numble.instagram.domain.post.entity.Feed;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.user.entity.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static org.jeasy.random.FieldPredicates.*;

public class FeedFixture {

    public static Feed create(User user, Post post) {
        return new Feed(user, post, post.getCreatedAt());
    }

    public static Feed createWithId(Long id, User user, Post post) {
        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Feed.class));

        var postPredicate = named("post")
                .and(ofType(Post.class))
                .and(inClass(Feed.class));

        var userPredicate = named("user")
                .and(ofType(User.class))
                .and(inClass(Feed.class));

        var param = new EasyRandomParameters()
                .randomize(idPredicate, () -> id)
                .randomize(userPredicate, () -> user)
                .randomize(postPredicate, () -> post)
                .dateRange(LocalDate.now(), LocalDate.now());

        return new EasyRandom(param).nextObject(Feed.class);
    }

    public static List<Feed> createFeeds(User user, List<Post> posts) {
        return posts.stream()
                .map(post -> create(user, post))
                .toList();
    }

    public static List<Feed> createFeedsWithIds(User user, List<Post> posts, List<Long> ids) {
        Iterator<Long> idIterator = ids.stream().iterator();
        return posts.stream()
                .map(post -> createWithId(idIterator.next() , user, post))
                .toList();
    }
}
