package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.post.dto.PostDto;
import com.numble.instagram.domain.post.service.PostWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.request.post.PostEditRequest;
import com.numble.instagram.dto.response.post.PostResponse;
import com.numble.instagram.support.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditPostUsecase {

    private final UserReadService userReadService;
    private final PostWriteService postWriteService;
    private final FileStore fileStore;

    public PostResponse execute(Long userId, Long postId, PostEditRequest postEditRequest) {
        User user = userReadService.getUser(userId);

        if (postEditRequest.postImageFile() == null || postEditRequest.postImageFile().isEmpty()) {
            PostDto editedPost = postWriteService.edit(user, postId, postEditRequest.content(), null);
            return PostResponse.from(editedPost.post());
        }

        String uploadedImage = fileStore.uploadImage(postEditRequest.postImageFile());
        PostDto editedPost = postWriteService.edit(user, postId, postEditRequest.content(), uploadedImage);
        fileStore.deleteFile(editedPost.willDeleteImageUrl());
        return PostResponse.from(editedPost.post());
    }
}
