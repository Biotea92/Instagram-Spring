package com.numble.instagram.presentation.follow;

import com.numble.instagram.application.auth.token.TokenProvider;
import com.numble.instagram.application.usecase.follow.CreateFollowUsecase;
import com.numble.instagram.application.usecase.follow.DestroyFollowUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
class FollowControllerDocTest {

    private MockMvc mockMvc;
    @MockBean
    private CreateFollowUsecase createFollowUsecase;
    @MockBean
    private DestroyFollowUsecase destroyFollowUsecase;
    @MockBean
    private TokenProvider tokenProvider;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private static final String DOCUMENT_IDENTIFIER = "follow/{method-name}/";


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
    @DisplayName("언팔로우는 완료되어야 한다.")
    void unfollow() throws Exception {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        String authorizationHeader = "Bearer access-token";
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(fromUserId);
        given(destroyFollowUsecase.execute(eq(fromUserId), eq(toUserId))).willReturn(toUserId);

        mockMvc.perform(post("/api/follow/unfollow/{toUserId}", toUserId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(toUserId))
                .andDo(print())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("toUserId").description("언팔로우 할 유저 id")
                        ),
                        responseFields(
                                fieldWithPath("userId").description("언팔로우 한 유저 id")
                        )
                ));
    }

    @Test
    @DisplayName("팔로우는 완료되어야 한다.")
    void follow() throws Exception {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        String authorizationHeader = "Bearer access-token";
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(fromUserId);
        given(createFollowUsecase.execute(eq(fromUserId), eq(toUserId))).willReturn(toUserId);

        mockMvc.perform(post("/api/follow/{toUserId}", toUserId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(toUserId))
                .andDo(print())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("toUserId").description("팔로우 할 유저 id")
                        ),
                        responseFields(
                                fieldWithPath("userId").description("팔로우 한 유저 id")
                        )
                ));
    }
}