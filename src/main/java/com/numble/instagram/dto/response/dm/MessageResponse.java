package com.numble.instagram.dto.response.dm;

import com.numble.instagram.domain.dm.entity.Message;

public record MessageResponse(Long userId, String content) {

    public static MessageResponse from(Message message) {
        return new MessageResponse(message.getToUser().getId(), message.getContent());
    }
}
