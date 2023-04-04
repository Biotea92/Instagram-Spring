package com.numble.instagram.util.fixture.user;

import com.numble.instagram.domain.user.entity.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import static org.jeasy.random.FieldPredicates.*;

public class UserFixture {

    public static User create(Long userId, String nickname, String password) {
        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(User.class));

        var nicknamePredicate = named("nickname")
                .and(ofType(String.class))
                .and(inClass(User.class));

        var passwordPredicate = named("password")
                .and(ofType(String.class))
                .and(inClass(User.class));

        var param = new EasyRandomParameters()
                .randomize(idPredicate, () -> userId)
                .randomize(nicknamePredicate, () -> nickname)
                .randomize(passwordPredicate, () -> password);

        return new EasyRandom(param).nextObject(User.class);
    }

    public static User create(Long userId, String nickname) {
        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(User.class));

        var nicknamePredicate = named("nickname")
                .and(ofType(String.class))
                .and(inClass(User.class));

        var passwordPredicate = named("password")
                .and(ofType(String.class))
                .and(inClass(User.class));

        var param = new EasyRandomParameters()
                .randomize(idPredicate, () -> userId)
                .randomize(nicknamePredicate, () -> nickname)
                .excludeField(passwordPredicate);

        return new EasyRandom(param).nextObject(User.class);
    }

    public static User create(String nickname) {
        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(User.class));

        var nicknamePredicate = named("nickname")
                .and(ofType(String.class))
                .and(inClass(User.class));

        var passwordPredicate = named("password")
                .and(ofType(String.class))
                .and(inClass(User.class));

        var profileImageUrlPredicate = named("profileImageUrl")
                .and(ofType(String.class))
                .and(inClass(User.class));

        var param = new EasyRandomParameters()
                .randomize(nicknamePredicate, () -> nickname)
                .randomize(profileImageUrlPredicate, () -> "https://")
                .randomize(passwordPredicate, () -> "1234")
                .excludeField(idPredicate);

        return new EasyRandom(param).nextObject(User.class);
    }
}
