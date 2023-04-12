package com.numble.instagram.dto.response.dm;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.entity.Message;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatRoomWithMessageResponse(
        Long chatroomId,
        String nickname,
        String profileImageUrl,
        String lastMessageContent,
        LocalDateTime lastSentAt
) {

    public static ChatRoomWithMessageResponse from(ChatRoom chatRoom, Message lastMessage) {
        return ChatRoomWithMessageResponse.builder()
                .chatroomId(chatRoom.getId())
                .nickname(lastMessage.getFromUser().getNickname())
                .profileImageUrl(lastMessage.getFromUser().getProfileImageUrl())
                .lastMessageContent(lastMessage.getContent())
                .lastSentAt(lastMessage.getSentAt())
                .build();
    }
}
