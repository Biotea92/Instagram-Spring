package com.numble.instagram.support.paging;

import java.util.List;

public record PageCursor<T>(CursorRequest nextCursorRequest, List<T> body) {
}
