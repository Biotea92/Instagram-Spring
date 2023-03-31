package com.numble.instagram.domain.user.service;

import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.repository.UserRepository;
import com.numble.instagram.dto.request.user.UserJoinRequest;
import com.numble.instagram.dto.response.user.UserResponse;
import com.numble.instagram.support.file.FileStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserWriteServiceTest {

    @InjectMocks
    private UserWriteService userWriteService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private FileStore fileStore;

    @Test
    void join_shouldReturnCreatedUserResponse() {
        UserJoinRequest userJoinRequest = new UserJoinRequest("testuser", "password", null);
        String encodedPassword = "encodedPassword";
        String profileImageUrl = "https://numble/user1234.jpg";
        User newUser = User.create(userJoinRequest.nickname(), encodedPassword, profileImageUrl);
        User savedUser = User.create("testuser", encodedPassword, profileImageUrl);
        when(passwordEncoder.encode(userJoinRequest.password())).thenReturn(encodedPassword);
        when(fileStore.uploadImage(userJoinRequest.profileImageFile())).thenReturn(profileImageUrl);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse userResponse = userWriteService.join(userJoinRequest);

        assertEquals(newUser.getNickname(), userResponse.nickname());
        assertEquals(newUser.getProfileImageUrl(), userResponse.profileImageUrl());
    }
}