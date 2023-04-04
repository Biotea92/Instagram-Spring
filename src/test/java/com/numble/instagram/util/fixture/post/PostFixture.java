package com.numble.instagram.util.fixture.post;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.user.entity.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDate;

import static org.jeasy.random.FieldPredicates.*;

public class PostFixture {

    public static Post create(String postImageUrl, String content, User writerUser) {
        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Post.class));

        var postImageUrlPredicate = named("postImageUrl")
                .and(ofType(String.class))
                .and(inClass(Post.class));

        var contentPredicate = named("content")
                .and(ofType(String.class))
                .and(inClass(Post.class));

        var writerUserPredicate = named("writerUser")
                .and(ofType(User.class))
                .and(inClass(Post.class));

        var likeCountPredicate = named("likeCount")
                .and(ofType(Long.class))
                .and(inClass(Post.class));

        var param = new EasyRandomParameters()
                .randomize(postImageUrlPredicate, () -> postImageUrl)
                .randomize(contentPredicate, () -> content)
                .randomize(writerUserPredicate, () -> writerUser)
                .randomize(likeCountPredicate, () -> 0L)
                .dateRange(LocalDate.now(), LocalDate.now())
                .excludeField(idPredicate);

        return new EasyRandom(param).nextObject(Post.class);
    }

    public static Post create(String content) {
        var contentPredicate = named("content")
                .and(ofType(String.class))
                .and(inClass(Post.class));

        var param = new EasyRandomParameters()
                .randomize(contentPredicate, () -> content)
                .dateRange(LocalDate.now(), LocalDate.now());

        return new EasyRandom(param).nextObject(Post.class);
    }
}
