package com.numble.instagram.domain.user.service;

import com.numble.instagram.support.file.FileStore;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.repository.UserRepository;
import com.numble.instagram.dto.request.user.UserJoinRequest;
import com.numble.instagram.dto.response.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserWriteService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStore fileStore;

    public UserResponse join(UserJoinRequest userJoinRequest) {
        String encodedPassword = passwordEncoder.encode(userJoinRequest.password());
        String profileImageUrl = fileStore.uploadImage(userJoinRequest.profileImageFile());
        User newUser = User.create(userJoinRequest.nickname(), encodedPassword, profileImageUrl);
        userRepository.save(newUser);
        return UserResponse.create(newUser);
    }
}