package com.numble.instagram.domain.follow.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.numble.instagram.domain.follow.entity.QFollow.follow;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long getFollowerCount(Long userId) {
        return jpaQueryFactory.select(follow.count())
                .from(follow)
                .where(follow.toUser.id.eq(userId))
                .fetchOne();
    }

    @Override
    public Long getFollowingCount(Long userId) {
        return jpaQueryFactory.select(follow.count())
                .from(follow)
                .where(follow.fromUser.id.eq(userId))
                .fetchOne();
    }
}
