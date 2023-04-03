package com.numble.instagram.dto.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record PostCreateRequest(@NotBlank String content, @NotNull MultipartFile postImageFile) {
}
