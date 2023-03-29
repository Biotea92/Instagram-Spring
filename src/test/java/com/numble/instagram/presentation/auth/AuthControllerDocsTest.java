package com.numble.instagram.presentation.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.instagram.application.auth.AuthService;
import com.numble.instagram.dto.common.LoginDto;
import com.numble.instagram.dto.request.auth.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                        .withResponseDefaults(prettyPrint())) // , modifyHeaders().remove("Vary")
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
                                headerWithName("Set-Cookie").description("Refresh Token")
                        ),
                        responseCookies(
                                cookieWithName("refreshToken").description("Refresh Token")
                        )
                ));
    }
}