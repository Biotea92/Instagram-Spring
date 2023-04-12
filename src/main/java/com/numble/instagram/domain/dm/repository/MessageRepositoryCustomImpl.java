package com.numble.instagram.domain.dm.repository;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.entity.Message;
import com.numble.instagram.domain.user.entity.QUser;
import com.numble.instagram.support.paging.CursorRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.numble.instagram.domain.dm.entity.QMessage.message;

@RequiredArgsConstructor
public class MessageRepositoryCustomImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Message> findAllByLessThanIdAndChatRoom(ChatRoom chatRoom, CursorRequest cursorRequest) {
        QUser fromUserAlias = new QUser("fromUser");
        QUser toUserAlias = new QUser("toUser");

        return jpaQueryFactory.selectFrom(message)
                .leftJoin(message.fromUser, fromUserAlias).fetchJoin()
                .leftJoin(message.toUser, toUserAlias).fetchJoin()
                .where(
                        message.chatRoom.eq(chatRoom),
                        lessThanMessageId(cursorRequest)
                )
                .orderBy(message.sentAt.desc())
                .limit(cursorRequest.size())
                .fetch();
    }

    private BooleanExpression lessThanMessageId(CursorRequest cursorRequest) {
        if (cursorRequest.hasKey()) {
            return message.id.lt(cursorRequest.key());
        }
        return null;
    }
}
