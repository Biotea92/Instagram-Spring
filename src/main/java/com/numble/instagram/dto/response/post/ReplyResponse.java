package com.numble.instagram.dto.response.post;

import com.numble.instagram.domain.post.entity.Reply;

import java.time.LocalDateTime;

public record ReplyResponse(Long id, String content, LocalDateTime createdAt) {

    public static ReplyResponse from(Reply reply) {
        return new ReplyResponse(reply.getId(), reply.getContent(), reply.getCreatedAt());
    }
}
