package com.numble.instagram.presentation;

import com.numble.instagram.exception.badrequest.InvalidRequestException;
import com.numble.instagram.exception.forbidden.ForbiddenException;
import com.numble.instagram.exception.notfound.NotFoundException;
import com.numble.instagram.exception.unauthorized.UnauthorizedException;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class CommonControllerAdviceTest {

    MockMvc mockMvc;

    @BeforeAll
    void setUp() {
        AdviceTestController controller = new AdviceTestController();
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(CommonControllerAdvice.class)
                .build();
    }

    @Test
    @DisplayName("UnauthorizedException 터지면 controllerAdvice가 작동한다.")
    void occurUnauthorizedException() throws Exception {
        mockMvc.perform(get("/unauthorized"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("인증되지 않은 사용자입니다."));
    }

    @Test
    @DisplayName("NotFoundException 터지면 controllerAdvice가 작동한다.")
    void occurNotFoundException() throws Exception {
        mockMvc.perform(get("/notFound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("리소스를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("ForbiddenException 터지면 controllerAdvice가 작동한다.")
    void occurForbiddenException() throws Exception {
        mockMvc.perform(get("/forbidden"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.message").value("접근 권한이 없습니다."));
    }

    @Test
    @DisplayName("InvalidRequestException 터지면 controllerAdvice가 작동한다.")
    void occurInvalidRequestException() throws Exception {
        mockMvc.perform(get("/invalidRequest"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));
    }

    @Test
    @DisplayName("BindException 터지면 controllerAdvice가 작동한다.")
    void occurBindException() throws Exception {
        mockMvc.perform(post("/badRequest")
                        .param("nickname", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));
    }

    @Test
    @DisplayName("RuntimeException 터지면 controllerAdvice가 작동한다.")
    void occurRuntimeException() throws Exception {
        mockMvc.perform(get("/runtime"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500));
    }

    @RestController
    static class AdviceTestController {
        @GetMapping("/unauthorized")
        public void unauthorized() {
            throw new UnauthorizedException();
        }

        @GetMapping("/notFound")
        public void notFound() {
            throw new NotFoundException();
        }

        @GetMapping("/forbidden")
        public void forbidden() {
            throw new ForbiddenException();
        }

        @GetMapping("/invalidRequest")
        public void invalidRequest() {
            throw new InvalidRequestException();
        }

        @PostMapping("/badRequest")
        public void badRequest(@Validated ValidationTestDto dto) {

        }

        @GetMapping("/runtime")
        public void runtime() {
            throw new IllegalArgumentException();
        }
    }

    record ValidationTestDto(@NotBlank String nickname) {
    }
}