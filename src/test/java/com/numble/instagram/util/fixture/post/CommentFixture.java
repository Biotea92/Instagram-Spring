package com.numble.instagram.util.fixture.post;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.user.entity.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDate;
import java.util.List;

import static org.jeasy.random.FieldPredicates.*;

public class CommentFixture {

    public static Comment create(User user, Post post, String content) {
        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Comment.class));

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
                .excludeField(idPredicate)
                .dateRange(LocalDate.now(), LocalDate.now());

        return new EasyRandom(param).nextObject(Comment.class);
    }

    public static Comment create(User user, Post post) {
        var commentWriterUserPredicate = named("commentWriteUser")
                .and(ofType(User.class))
                .and(inClass(Comment.class));

        var postPredicate = named("post")
                .and(ofType(Post.class))
                .and(inClass(Comment.class));

        var param = new EasyRandomParameters()
                .randomize(postPredicate, () -> post)
                .randomize(commentWriterUserPredicate, () -> user)
                .dateRange(LocalDate.now(), LocalDate.now());

        return new EasyRandom(param).nextObject(Comment.class);
    }

    public static Comment create(String content) {
        var contentPredicate = named("content")
                .and(ofType(String.class))
                .and(inClass(Comment.class));

        var param = new EasyRandomParameters()
                .randomize(contentPredicate, () -> content)
                .dateRange(LocalDate.now(), LocalDate.now());

        return new EasyRandom(param).nextObject(Comment.class);
    }

    public static List<Comment> createComments(User user, List<Post> posts) {
        return posts.stream()
                .map(post -> create(user, post, "comment-content"))
                .toList();
    }
}
