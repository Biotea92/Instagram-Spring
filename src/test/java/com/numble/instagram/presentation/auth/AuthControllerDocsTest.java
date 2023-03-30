package com.numble.instagram.presentation.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.instagram.application.auth.AuthService;
import com.numble.instagram.dto.common.LoginDto;
import com.numble.instagram.dto.request.auth.LoginRequest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.cookies.CookieDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
class AuthControllerDocsTest {

    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private static final String DOCUMENT_IDENTIFIER = "auth/{method-name}/";

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint(), modifyHeaders().remove("Vary")))
                .build();
    }

    @Test
    @DisplayName("login 요청시 로그인이 완료 되어야 한다.")
    void login() throws Exception {
        String nickname = "test_user";
        String password = "test1234";
        String accessToken = "test_access_token";
        String refreshToken = "test_refresh_token";
        when(authService.login(nickname, password)).thenReturn(new LoginDto(accessToken, refreshToken));

        LoginRequest loginRequest = new LoginRequest(nickname, password);
        String json = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(cookie().value("refreshToken", refreshToken))
                .andExpect(cookie().maxAge("refreshToken", 604800))
                .andExpect(cookie().httpOnly("refreshToken", true))
                .andExpect(cookie().secure("refreshToken", true))
                .andExpect(cookie().path("refreshToken", "/"))
                .andDo(print())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        requestFields(
                                fieldWithPath("nickname").description("nickname"),
                                fieldWithPath("password").description("password")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("Access Token")
                        ),
                        responseHeaders(
                                headerWithName("Set-Cookie").description("Refresh Token header")
                        ),
                        responseCookies(
                                cookieWithName("refreshToken").description("Refresh Token cookie")
                        )
                ));
    }

    @Test
    @DisplayName("logout 요청시 로그아웃 되어야한다.")
    void logout() throws Exception {
        String refreshToken = "valid_refresh_token";

        mockMvc.perform(get("/api/auth/logout")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andExpect(status().isNoContent())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refreshToken")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Max-Age=0")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("HttpOnly")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Secure")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Path=/")))
                .andDo(document(DOCUMENT_IDENTIFIER,
                        requestCookies(
                                cookieWithName("refreshToken").description("Refresh Token cookie")
                        ),
                        responseHeaders(
                                headerWithName("Set-Cookie").description("Refresh Token header")
                        )
                ));
    }

    @Test
    @DisplayName("reissue 요청시 토큰을 재발급한다.")
    void reissueToken() throws Exception {
        String refreshToken = "valid_refresh_token";
        String newRefreshToken = "new_refresh_token";
        LoginDto loginDto = new LoginDto("new_access_token", newRefreshToken);
        when(authService.reissueToken(refreshToken)).thenReturn(loginDto);

        mockMvc.perform(post("/api/auth/reissueToken")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andExpect(status().isOk())
                .andExpect(cookie().value("refreshToken", newRefreshToken))
                .andExpect(cookie().maxAge("refreshToken", 604800))
                .andExpect(cookie().httpOnly("refreshToken", true))
                .andExpect(cookie().secure("refreshToken", true))
                .andExpect(cookie().path("refreshToken", "/"))
                .andDo(document(DOCUMENT_IDENTIFIER,
                        requestCookies(
                                cookieWithName("refreshToken").description("Refresh Token cookie")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("Access Token")
                        ),
                        responseHeaders(
                                headerWithName("Set-Cookie").description("Refresh Token header")
                        ),
                        responseCookies(
                                cookieWithName("refreshToken").description("Refresh Token cookie")
                        )
                ));
    }
}