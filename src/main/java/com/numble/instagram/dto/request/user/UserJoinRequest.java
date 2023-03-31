package com.numble.instagram.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record UserJoinRequest(@NotBlank String nickname, @NotBlank String password, MultipartFile profileImageFile) {
}
