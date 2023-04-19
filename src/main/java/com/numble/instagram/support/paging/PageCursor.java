package com.numble.instagram.support.paging;

import java.util.List;

public record PageCursor<T>(CursorRequest nextCursorRequest, List<T> body) {

    public static <T> PageCursor<T> fromResponse(
            CursorRequest nextCursorRequest, List<T> body) {
        return new PageCursor<>(nextCursorRequest, body);
    }
}
