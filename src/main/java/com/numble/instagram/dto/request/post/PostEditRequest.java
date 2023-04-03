package com.numble.instagram.dto.request.post;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record PostEditRequest(@NotBlank String content, MultipartFile postImageFile) {
}
