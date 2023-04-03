package com.numble.instagram.dto.request.post;

import com.numble.instagram.exception.badrequest.ImageFileNotExistsException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record PostCreateRequest(@NotBlank String content, MultipartFile postImageFile) {

    public PostCreateRequest(String content, MultipartFile postImageFile) {
        this.content = content;
        validatePostImageFile(postImageFile);
        this.postImageFile = postImageFile;
    }

    private void validatePostImageFile(MultipartFile postImageFile) {
        if (postImageFile == null || postImageFile.isEmpty()) {
            throw new ImageFileNotExistsException();
        }
    }
}
