package com.numble.instagram.domain.dm.repository;

import com.numble.instagram.domain.dm.dto.ChatRoomWithLastMessage;
import com.numble.instagram.domain.dm.entity.QMessage;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.support.paging.CursorRequest;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.numble.instagram.domain.dm.entity.QChatRoom.chatRoom;
import static com.numble.instagram.domain.dm.entity.QMessage.message;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChatRoomWithLastMessage> findChatRoomsByUserWithLatestMessage(User user, CursorRequest cursorRequest) {
        QMessage latestMessage = new QMessage("latestMessage");

        List<Tuple> tuples = jpaQueryFactory.select(chatRoom, latestMessage)
                .from(chatRoom)
                .distinct()
                .leftJoin(chatRoom.messages, latestMessage)
                .on(
                        latestMessage.id.eq(
                                jpaQueryFactory.select(message.id.max())
                                        .from(message)
                                        .where(message.chatRoom.eq(chatRoom))))
                .where(
                        isChatRoomUser(user),
                        lessThanChatRoomId(cursorRequest)
                )
                .orderBy(chatRoom.id.desc())
                .limit(cursorRequest.size())
                .fetch();

        return tuples.stream()
                .map(tuple -> new ChatRoomWithLastMessage(tuple.get(chatRoom), tuple.get(latestMessage)))
                .toList();
    }

    private BooleanExpression isChatRoomUser(User user) {
        return chatRoom.user1.eq(user).or(chatRoom.user2.eq(user));
    }

    private BooleanExpression lessThanChatRoomId(CursorRequest cursorRequest) {
        if (cursorRequest.hasKey()) {
            return chatRoom.id.lt(cursorRequest.key());
        }
        return null;
    }
}
