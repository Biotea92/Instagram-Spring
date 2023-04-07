package com.numble.instagram.dto.response.post;

import com.numble.instagram.domain.post.entity.Reply;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReplyDetailResponse(
        Long replyId,
        String content,
        LocalDateTime createdAt,
        Long userId,
        String nickname,
        String profileImageUrl) {

    public static ReplyDetailResponse from(Reply reply) {
        return ReplyDetailResponse.builder()
                .replyId(reply.getId())
                .content(reply.getContent())
                .createdAt(reply.getCreatedAt())
                .userId(reply.getReplyWriteUser().getId())
                .nickname(reply.getReplyWriteUser().getNickname())
                .profileImageUrl(reply.getReplyWriteUser().getProfileImageUrl())
                .build();
    }
}
