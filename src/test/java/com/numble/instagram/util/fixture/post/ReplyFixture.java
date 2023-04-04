package com.numble.instagram.util.fixture.post;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Reply;
import com.numble.instagram.domain.user.entity.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDate;

import static org.jeasy.random.FieldPredicates.*;

public class ReplyFixture {

    public static Reply create(User user, Comment comment, String content) {
        var contentPredicate = named("content")
                .and(ofType(String.class))
                .and(inClass(Reply.class));

        var replyWriteUserPredicate = named("replyWriteUser")
                .and(ofType(User.class))
                .and(inClass(Reply.class));

        var commentPredicate = named("comment")
                .and(ofType(Comment.class))
                .and(inClass(Reply.class));

        var param = new EasyRandomParameters()
                .randomize(contentPredicate, () -> content)
                .randomize(commentPredicate, () -> comment)
                .randomize(replyWriteUserPredicate, () -> user)
                .dateRange(LocalDate.now(), LocalDate.now());

        return new EasyRandom(param).nextObject(Reply.class);
    }
}
