package com.numble.instagram.presentation.follow;

import com.numble.instagram.application.usecase.CreateFollowUserUsecase;
import com.numble.instagram.application.usecase.DestroyFollowUserUsecase;
import com.numble.instagram.presentation.auth.AuthenticatedUserResolver;
import com.numble.instagram.presentation.auth.AuthenticationInterceptor;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
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
    private CreateFollowUserUsecase createFollowUserUsecase;
    @MockBean
    private DestroyFollowUserUsecase destroyFollowUserUsecase;
    @MockBean
    private AuthenticationInterceptor interceptor;
    @MockBean
    private AuthenticatedUserResolver authenticatedUserResolver;
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

        when(interceptor.preHandle(any(),any(),any())).thenReturn(true);
    }

    @Test
    @DisplayName("팔로우는 완료되어야 한다.")
    void follow() throws Exception {
        Long fromUserId = 1L;
        Long toUserId = 2L;

        when(authenticatedUserResolver.supportsParameter(any())).thenReturn(true);
        when(authenticatedUserResolver.resolveArgument(any(),any(),any(),any())).thenReturn(fromUserId);
        given(destroyFollowUserUsecase.execute(eq(fromUserId), eq(toUserId))).willReturn(toUserId);

        mockMvc.perform(post("/api/follow/unfollow/{toUserId}", toUserId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
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
    @DisplayName("언팔로우는 완료되어야 한다.")
    void unfollow() throws Exception {
        Long fromUserId = 1L;
        Long toUserId = 2L;

        when(authenticatedUserResolver.supportsParameter(any())).thenReturn(true);
        when(authenticatedUserResolver.resolveArgument(any(),any(),any(),any())).thenReturn(fromUserId);
        given(createFollowUserUsecase.execute(eq(fromUserId), eq(toUserId))).willReturn(toUserId);

        mockMvc.perform(post("/api/follow/{toUserId}", toUserId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
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