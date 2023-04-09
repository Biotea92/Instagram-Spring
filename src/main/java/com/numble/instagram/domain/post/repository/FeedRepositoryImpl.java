package com.numble.instagram.domain.post.repository;

import com.numble.instagram.domain.post.entity.Feed;
import com.numble.instagram.support.paging.CursorRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.numble.instagram.domain.post.entity.QFeed.feed;
import static com.numble.instagram.domain.post.entity.QPost.post;
import static com.numble.instagram.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Feed> findAllByLessThanIdAndUserId(Long userId, CursorRequest cursorRequest) {
        return jpaQueryFactory.selectFrom(feed)
                .leftJoin(feed.post, post).fetchJoin()
                .leftJoin(post.writeUser, user).fetchJoin()
                .where(
                        feed.user.id.eq(userId),
                        lessThanFeedId(cursorRequest)
                )
                .orderBy(feed.createdAt.desc())
                .limit(cursorRequest.size())
                .fetch();
    }

    private BooleanExpression lessThanFeedId(CursorRequest cursorRequest) {
        if (cursorRequest.hasKey()) {
            return feed.id.lt(cursorRequest.key());
        }
        return null;
    }
}
