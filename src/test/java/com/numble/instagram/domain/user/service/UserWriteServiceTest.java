package com.numble.instagram.domain.user.service;

import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.repository.UserRepository;
import com.numble.instagram.dto.request.user.UserJoinRequest;
import com.numble.instagram.dto.response.user.UserResponse;
import com.numble.instagram.support.file.FileStore;
import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
    @DisplayName("회원가입은 완료되어야 한다.")
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

    @Test
    @DisplayName("새로운 닉네임과 프로필이미지가 주어지면 유저의 정보가 업데이트된다.")
    void edit_userFound_nicknameChangedAndProfileImageUploaded() {
        String newNickname = "newNickname";
        String newProfileImageUrl = "newProfileImageUrl";
        MockMultipartFile newProfileImageFile = new MockMultipartFile(
                "newProfileImageFile", "test.jpg", "image/jpeg", "test image".getBytes());
        User existingUser = UserFixture.create(1L, "oldNickname", "1234");

        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(fileStore.uploadImage(newProfileImageFile)).thenReturn(newProfileImageUrl);

        UserResponse updatedUser = userWriteService.edit(existingUser.getId(), newNickname, newProfileImageFile);

        assertEquals(newNickname, updatedUser.nickname());
        assertEquals(newProfileImageUrl, updatedUser.profileImageUrl());
    }

    @Test
    @DisplayName("새로운 닉네임만 주어지고 프로필이미지가 주어지지 않으면 닉네임만 업데이트된다.")
    void edit_nickname_notProfileImage() {
        String newNickname = "newNickname";
        User existingUser = UserFixture.create(1L, "oldNickname", "1234");

        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));

        UserResponse updatedUser = userWriteService.edit(existingUser.getId(), newNickname, null);

        assertEquals(newNickname, updatedUser.nickname());
        assertEquals(existingUser.getProfileImageUrl(), updatedUser.profileImageUrl());
    }
}