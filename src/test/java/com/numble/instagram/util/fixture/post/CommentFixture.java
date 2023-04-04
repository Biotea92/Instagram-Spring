package com.numble.instagram.util.fixture.post;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.user.entity.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDate;

import static org.jeasy.random.FieldPredicates.*;

public class CommentFixture {

    public static Comment create(User user, Post post, String content) {
        var contentPredicate = named("content")
                .and(ofType(String.class))
                .and(inClass(Comment.class));

        var commentWriterUserPredicate = named("commentWriteUser")
                .and(ofType(User.class))
                .and(inClass(Comment.class));

        var postPredicate = named("post")
                .and(ofType(Post.class))
                .and(inClass(Comment.class));

        var param = new EasyRandomParameters()
                .randomize(contentPredicate, () -> content)
                .randomize(postPredicate, () -> post)
                .randomize(commentWriterUserPredicate, () -> user)
                .dateRange(LocalDate.now(), LocalDate.now());

        return new EasyRandom(param).nextObject(Comment.class);
    }
}
