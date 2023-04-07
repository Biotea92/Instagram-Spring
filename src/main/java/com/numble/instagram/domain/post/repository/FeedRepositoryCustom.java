package com.numble.instagram.domain.post.repository;

import com.numble.instagram.domain.post.entity.Feed;
import com.numble.instagram.support.paging.CursorRequest;

import java.util.List;

public interface FeedRepositoryCustom {

    List<Feed> findAllByLessThanIdAndUserId(Long userId, CursorRequest cursorRequest);
}
