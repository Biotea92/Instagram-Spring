package com.numble.instagram.support.paging;

import com.numble.instagram.dto.response.dm.ChatRoomWithMessageResponse;
import com.numble.instagram.dto.response.dm.MessageDetailResponse;
import com.numble.instagram.dto.response.post.PostDetailResponse;

import java.util.List;

public record PageCursor<T>(CursorRequest nextCursorRequest, List<T> body) {

    public static PageCursor<PostDetailResponse> fromPostDetailResponse(
            CursorRequest nextCursorRequest, List<PostDetailResponse> posts) {
        return new PageCursor<>(nextCursorRequest, posts);
    }

    public static PageCursor<MessageDetailResponse> fromMessageDetailResponse(
            CursorRequest nextCursorRequest, List<MessageDetailResponse> messages) {
        return new PageCursor<>(nextCursorRequest, messages);
    }

    public static PageCursor<ChatRoomWithMessageResponse> fromChatRoomWithMessageResponse(
            CursorRequest nextCursorRequest, List<ChatRoomWithMessageResponse> chatRooms) {
        return new PageCursor<>(nextCursorRequest, chatRooms);
    }
}
