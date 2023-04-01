package com.numble.instagram.domain.user.service;

import com.numble.instagram.exception.notfound.UserNotFoundException;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.repository.UserRepository;
import com.numble.instagram.dto.request.user.UserJoinRequest;
import com.numble.instagram.dto.response.user.UserResponse;
import com.numble.instagram.exception.badrequest.DuplicatedUserException;
import com.numble.instagram.support.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserWriteService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStore fileStore;

    public UserResponse join(UserJoinRequest userJoinRequest) {
        validateDuplicateNickname(userJoinRequest.nickname());
        String encodedPassword = passwordEncoder.encode(userJoinRequest.password());
        String profileImageUrl = fileStore.uploadImage(userJoinRequest.profileImageFile());
        User newUser = User.create(userJoinRequest.nickname(), encodedPassword, profileImageUrl);
        userRepository.save(newUser);
        return UserResponse.from(newUser);
    }

    public UserResponse edit(Long userId, String newNickname, MultipartFile newProfileImageFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        user.changeNickname(newNickname);
        checkImageFileAndChange(newProfileImageFile, user);
        return UserResponse.from(user);
    }

    private void checkImageFileAndChange(MultipartFile newProfileImageFile, User user) {
        if (newProfileImageFile == null || !newProfileImageFile.isEmpty()) {
            fileStore.deleteFile(user.getProfileImageUrl());
            String newProfileImageUrl = fileStore.uploadImage(newProfileImageFile);
            user.changeProfileImageUrl(newProfileImageUrl);
        }
    }

    private void validateDuplicateNickname(String nickname) {
        Optional<User> DuplicatedUser = userRepository.findByNickname(nickname);
        if (DuplicatedUser.isPresent()) {
            throw new DuplicatedUserException();
        }
    }
}
