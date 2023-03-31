package com.numble.instagram.presentation.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("logout시 쿠키에 리프레쉬 토큰이 없으면 401 에러코드를 던진다.")
    void shouldThrowRefreshTokenNotExistsExceptionWhenRefreshTokenNotProvided() throws Exception {
        mockMvc.perform(get("/api/auth/logout"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("토큰 재발급 시 쿠키에 리프레쉬 토큰이 없으면 401 에러코드를 던진다.")
    void shouldThrowRefreshTokenNotExistsExceptionWhenRefreshTokenNotProvided2() throws Exception {
        mockMvc.perform(post("/api/auth/reissueToken"))
                .andExpect(status().isUnauthorized());
    }
}