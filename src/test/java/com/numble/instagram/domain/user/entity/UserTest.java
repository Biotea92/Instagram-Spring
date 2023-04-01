package com.numble.instagram.domain.user.entity;

import com.numble.instagram.util.fixture.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    @DisplayName("nickname은 수정되어야한다.")
    void changeNickname() {
        User user = UserFixture.create(1L, "oldNickname", "1234");
        user.changeNickname("newNickname");
        assertEquals("newNickname", user.getNickname());
    }

    @Test
    @DisplayName("같은 nickname은 수정되지않는다.")
    void changeSameNickname() {
        User user = UserFixture.create(1L, "oldNickname", "1234");
        user.changeNickname("oldNickname");
        assertEquals("oldNickname", user.getNickname());
    }
}