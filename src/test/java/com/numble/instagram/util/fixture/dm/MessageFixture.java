package com.numble.instagram.util.fixture.dm;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.entity.Message;
import com.numble.instagram.domain.user.entity.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import static org.jeasy.random.FieldPredicates.*;

public class MessageFixture {

    public static Message create(String content, User fromUser, User toUser, ChatRoom chatRoom) {
        return Message.create(content, fromUser, toUser, chatRoom);
    }

    public static Message create(Long id) {
        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Message.class));

        var param = new EasyRandomParameters()
                .randomize(idPredicate, () -> id);

        return new EasyRandom(param).nextObject(Message.class);
    }
}
