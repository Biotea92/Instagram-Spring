package com.numble.instagram.presentation.post;

import com.numble.instagram.application.auth.token.TokenProvider;
import com.numble.instagram.application.usecase.post.CreatePostUsecase;
import com.numble.instagram.application.usecase.post.EditPostUsecase;
import com.numble.instagram.dto.request.post.PostCreateRequest;
import com.numble.instagram.dto.request.post.PostEditRequest;
import com.numble.instagram.dto.response.post.PostResponse;
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
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
class PostControllerDocsTest {

    private MockMvc mockMvc;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private CreatePostUsecase createPostUsecase;
    @MockBean
    private EditPostUsecase editPostUsecase;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private static final String DOCUMENT_IDENTIFIER = "post/{method-name}/";
    MockMultipartFile postImageFile = new MockMultipartFile("postImageFile", "image".getBytes());

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
    @DisplayName("글은 등록되어야 한다.")
    void register() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);

        String content = "내용입니다.";
        PostCreateRequest postCreateRequest = new PostCreateRequest(content, postImageFile);
        PostResponse postResponse = new PostResponse(
                1L, "https://postImage.jpg", content, 0L, LocalDateTime.now());
        given(createPostUsecase.execute(eq(userId), eq(postCreateRequest))).willReturn(postResponse);

        mockMvc.perform(multipart("/api/post")
                        .file(postImageFile)
                        .param("content", content)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        requestParts(
                                partWithName("postImageFile").description("파일 업로드")
                        ),
//                        formParameters(
//                                parameterWithName("content").description("글 내용")
//                        ),
                        responseFields(
                                fieldWithPath("id").description("post id"),
                                fieldWithPath("postImageUrl").description("글 이미지 Url"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("likeCount").description("좋아요 수"),
                                fieldWithPath("createdAt").description("글 쓴 날짜")
                        )
                ));
    }

    @Test
    @DisplayName("글은 수정 되어야한다.")
    void edit() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        Long postId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);

        String content = "새로운 내용입니다.";
        postImageFile = new MockMultipartFile("postImageFile", "image".getBytes());
        PostEditRequest postEditRequest = new PostEditRequest(content, postImageFile);
        PostResponse postResponse = new PostResponse(
                1L, "https://new-image.jpg", content, 0L, LocalDateTime.now());
        given(editPostUsecase.execute(eq(userId), eq(postId), eq(postEditRequest))).willReturn(postResponse);

        mockMvc.perform(multipart("/api/post/{postId}/edit", postId)
                        .file(postImageFile)
                        .param("content", content)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postId))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.postImageUrl").value("https://new-image.jpg"))
                .andDo(document(DOCUMENT_IDENTIFIER,
                        requestParts(
                                partWithName("postImageFile").description("파일 업로드")
                        ),
                        pathParameters(
                                parameterWithName("postId").description("글 id")
                        ),
//                        formParameters(
//                                parameterWithName("content").description("글 내용")
//                        ),
                        responseFields(
                                fieldWithPath("id").description("post id"),
                                fieldWithPath("postImageUrl").description("글 이미지 Url"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("likeCount").description("좋아요 수"),
                                fieldWithPath("createdAt").description("글 쓴 날짜")
                        )
                ));
    }
}