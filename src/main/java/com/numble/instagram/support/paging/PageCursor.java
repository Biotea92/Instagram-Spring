package com.numble.instagram.support.paging;

import com.numble.instagram.dto.response.post.PostDetailResponse;

import java.util.List;

public record PageCursor<T>(CursorRequest nextCursorRequest, List<T> posts) {

    public static PageCursor<PostDetailResponse> from(CursorRequest nextCursorRequest, List<PostDetailResponse> posts) {
        return new PageCursor<>(nextCursorRequest, posts);
    }
}
