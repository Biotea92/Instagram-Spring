package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Feed;
import com.numble.instagram.domain.post.repository.FeedRepository;
import com.numble.instagram.support.paging.CursorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedReadService {

    private final FeedRepository feedRepository;

    public List<Feed> getFeeds(Long userId, CursorRequest request) {
        return feedRepository.findAllByLessThanIdAndUserId(userId, request);
    }
}
