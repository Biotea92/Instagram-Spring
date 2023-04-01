package com.numble.instagram.presentation.user;

import com.numble.instagram.application.auth.token.TokenProvider;
import com.numble.instagram.domain.user.service.UserWriteService;
import com.numble.instagram.dto.request.user.UserEditRequest;
import com.numble.instagram.dto.request.user.UserJoinRequest;
import com.numble.instagram.dto.response.user.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
class UserControllerDocTest {

    private MockMvc mockMvc;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private UserWriteService userWriteService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private static final String DOCUMENT_IDENTIFIER = "user/{method-name}/";

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
    @DisplayName("회원가입은 완료되어야한다.")
    void join() throws Exception {
        MockMultipartFile profileImageFile = new MockMultipartFile("profileImageFile", "image".getBytes());

        UserResponse expectedUserResponse = new UserResponse(
                1L, "test_user", "https://test_image_url", LocalDateTime.now());

        UserJoinRequest userJoinRequest = new UserJoinRequest("test_user", "qwer1234", profileImageFile);
        given(userWriteService.join(eq(userJoinRequest))).willReturn(expectedUserResponse);

        mockMvc.perform(multipart("/api/user")
                        .file(profileImageFile)
                        .param("nickname", "test_user")
                        .param("password", "qwer1234"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        requestParts(
                                partWithName("profileImageFile").description("파일 업로드")
                        ),
//                        formParameters(
//                                parameterWithName("nickname").description("닉네임"),
//                                parameterWithName("password").description("비밀번호")
//                        ),
                        responseFields(
                                fieldWithPath("id").description("유저 id"),
                                fieldWithPath("nickname").description("유저 nickname"),
                                fieldWithPath("profileImageUrl").description("유저 프로필 이미지 Url"),
                                fieldWithPath("joinedAt").description("유저 가입일")
                        )
                ));
    }

    @Test
    @DisplayName("프로필 수정은 완료되어야한다.")
    void edit() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);

        MockMultipartFile profileImageFile = new MockMultipartFile("profileImageFile", "image".getBytes());
        UserResponse expectedUserResponse = new UserResponse(
                userId, "new_test_user", "https://new_test_image_url", LocalDateTime.now());

        UserEditRequest userEditRequest = new UserEditRequest("new_test_user", profileImageFile);
        given(userWriteService.edit(eq(userId), eq(userEditRequest.nickname()), eq(userEditRequest.profileImageFile())))
                .willReturn(expectedUserResponse);

        mockMvc.perform(multipart("/api/user/edit")
                        .file(profileImageFile)
                        .param("nickname", "new_test_user")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        requestParts(
                                partWithName("profileImageFile").description("파일 업로드")
                        ),
//                        formParameters(
//                                parameterWithName("nickname").description("닉네임"),
//                                parameterWithName("password").description("비밀번호")
//                        ),
                        responseFields(
                                fieldWithPath("id").description("유저 id"),
                                fieldWithPath("nickname").description("수정 된 유저 nickname"),
                                fieldWithPath("profileImageUrl").description("수정 된 프로필 이미지 Url"),
                                fieldWithPath("joinedAt").description("유저 가입일")
                        )
                ));
    }
}