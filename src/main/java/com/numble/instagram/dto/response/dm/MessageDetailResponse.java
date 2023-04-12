package com.numble.instagram.dto.response.dm;

import com.numble.instagram.domain.dm.entity.Message;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageDetailResponse(
        Long id,
        Long fromUserId,
        String fromUserNickname,
        String fromUserProfileUrl,
        Long toUserId,
        String toUserNickname,
        String toUserProfileUrl,
        String content,
        String messageType,
        LocalDateTime sentAt) {

    public static MessageDetailResponse from(Message message) {
        return MessageDetailResponse.builder()
                .id(message.getId())
                .fromUserId(message.getFromUser().getId())
                .fromUserNickname(message.getFromUser().getNickname())
                .fromUserProfileUrl(message.getFromUser().getProfileImageUrl())
                .toUserId(message.getToUser().getId())
                .toUserNickname(message.getToUser().getNickname())
                .toUserProfileUrl(message.getToUser().getProfileImageUrl())
                .content(message.getContent())
                .messageType(message.getMessageType().name())
                .sentAt(message.getSentAt())
                .build();
    }
}
