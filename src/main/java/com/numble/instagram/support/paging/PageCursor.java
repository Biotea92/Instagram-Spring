package com.numble.instagram.support.paging;

import java.util.List;

public record PageCursor<T>(CursorRequest nextCursorRequest, List<T> body) {

    public static PageCursor<?> from(CursorRequest nextCursorRequest, List<?> body) {
        return new PageCursor<>(nextCursorRequest, body);
    }
}
