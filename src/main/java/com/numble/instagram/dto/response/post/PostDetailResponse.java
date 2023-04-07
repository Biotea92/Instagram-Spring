package com.numble.instagram.dto.response.post;

import com.numble.instagram.domain.post.entity.Post;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostDetailResponse(
        Long postId,
        String content,
        String postImageUrl,
        Long likeCount,
        LocalDateTime createdAt,
        LocalDate createdDate,
        Long userId,
        String nickname,
        String profileImageUrl,
        Boolean isPostLiked,
        List<CommentDetailResponse> comments) {

    public static PostDetailResponse from(Post post, Boolean isPostLiked) {
        return PostDetailResponse.builder()
                .postId(post.getId())
                .content(post.getContent())
                .postImageUrl(post.getPostImageUrl())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .createdDate(post.getCreatedDate())
                .userId(post.getWriteUser().getId())
                .nickname(post.getWriteUser().getNickname())
                .profileImageUrl(post.getWriteUser().getProfileImageUrl())
                .isPostLiked(isPostLiked)
                .comments(post.getComments().stream().map(CommentDetailResponse::from).toList())
                .build();
    }
}
