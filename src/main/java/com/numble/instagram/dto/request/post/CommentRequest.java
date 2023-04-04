package com.numble.instagram.dto.request.post;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(@NotBlank String content) {
}
