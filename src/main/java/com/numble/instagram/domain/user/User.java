package com.numble.instagram.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String nickname;

    private String password;

    @Builder
    public User(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }
}
