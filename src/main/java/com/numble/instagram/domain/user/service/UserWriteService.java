package com.numble.instagram.domain.user.service;

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
        return UserResponse.create(newUser);
    }

    private void validateDuplicateNickname(String nickname) {
        Optional<User> DuplicatedUser = userRepository.findByNickname(nickname);
        if (DuplicatedUser.isPresent()) {
            throw new DuplicatedUserException();
        }
    }
}
