package com.numble.instagram.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String nickname;

    private String password;

    private String profileImageUrl;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    private void onPrePersist() {
        this.joinedAt = LocalDateTime.now();
    }

    @Builder
    public User(String nickname, String password, String profileImageUrl) {
        this.nickname = nickname;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
    }

    public static User create(String nickname, String encodedPassword, String profileImageUrl) {
        return User.builder()
                .nickname(nickname)
                .password(encodedPassword)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public void changeNickname(String newNickname) {
        if (!nickname.equals(newNickname)) {
            this.nickname = newNickname;
        }
    }

    public void changeProfileImageUrl(String newProfileImageUrl) {
        this.profileImageUrl = newProfileImageUrl;
    }
}
