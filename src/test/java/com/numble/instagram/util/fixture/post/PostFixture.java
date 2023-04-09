package com.numble.instagram.util.fixture.post;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.user.entity.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static org.jeasy.random.FieldPredicates.*;

public class PostFixture {

    public static Post create(String postImageUrl, String content, User writeUser) {
        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Post.class));

        var postImageUrlPredicate = named("postImageUrl")
                .and(ofType(String.class))
                .and(inClass(Post.class));

        var contentPredicate = named("content")
                .and(ofType(String.class))
                .and(inClass(Post.class));

        var writerUserPredicate = named("writeUser")
                .and(ofType(User.class))
                .and(inClass(Post.class));

        var likeCountPredicate = named("likeCount")
                .and(ofType(Long.class))
                .and(inClass(Post.class));

        var param = new EasyRandomParameters()
                .randomize(postImageUrlPredicate, () -> postImageUrl)
                .randomize(contentPredicate, () -> content)
                .randomize(writerUserPredicate, () -> writeUser)
                .randomize(likeCountPredicate, () -> 0L)
                .dateRange(LocalDate.now(), LocalDate.now())
                .excludeField(idPredicate);

        return new EasyRandom(param).nextObject(Post.class);
    }

    public static Post create(String content) {
        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Post.class));

        var contentPredicate = named("content")
                .and(ofType(String.class))
                .and(inClass(Post.class));

        var param = new EasyRandomParameters()
                .randomize(contentPredicate, () -> content)
                .excludeField(idPredicate)
                .dateRange(LocalDate.now(), LocalDate.now());

        return new EasyRandom(param).nextObject(Post.class);
    }

    public static List<Post> createPosts(User user) {
        return IntStream.range(0, 20)
                .mapToObj(i -> create("imageUrl " + i, "post-content " + i, user))
                .toList();
    }
}
