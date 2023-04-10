package com.numble.instagram.presentation.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.instagram.application.auth.token.TokenProvider;
import com.numble.instagram.application.usecase.post.*;
import com.numble.instagram.dto.request.post.CommentRequest;
import com.numble.instagram.dto.request.post.PostCreateRequest;
import com.numble.instagram.dto.request.post.PostEditRequest;
import com.numble.instagram.dto.request.post.ReplyRequest;
import com.numble.instagram.dto.response.post.CommentResponse;
import com.numble.instagram.dto.response.post.PostLikeResponse;
import com.numble.instagram.dto.response.post.PostResponse;
import com.numble.instagram.dto.response.post.ReplyResponse;
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
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
class PostControllerDocsTest {

    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private CreatePostUsecase createPostUsecase;
    @MockBean
    private EditPostUsecase editPostUsecase;
    @MockBean
    private CreateCommentUsecase createCommentUsecase;
    @MockBean
    private EditCommentUsecase editCommentUsecase;
    @MockBean
    private CreateReplyUsecase createReplyUsecase;
    @MockBean
    private EditReplyUsecase editReplyUsecase;
    @MockBean
    private CreatePostLikeUsecase createPostLikeUsecase;
    @MockBean
    private DestroyPostLikeUsecase destroyPostLikeUsecase;
    @MockBean
    private DestroyPostUsecase destroyPostUsecase;
    @MockBean
    private DestroyCommentUsecase destroyCommentUsecase;
    @MockBean
    private DestroyReplyUsecase destroyReplyUsecase;
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

    @Test
    @DisplayName("댓글은 등록되어야한다.")
    void registerComment() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        Long postId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);

        CommentRequest commentRequest = new CommentRequest("댓글 입니다.");
        CommentResponse commentResponse =
                new CommentResponse(1L, commentRequest.content(), LocalDateTime.now());
        given(createCommentUsecase.execute(userId, postId, commentRequest)).willReturn(commentResponse);

        String json = objectMapper.writeValueAsString(commentRequest);

        mockMvc.perform(post("/api/post/{postId}/comment", postId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("postId").description("댓글 등록 할 글 id")
                        ),
                        requestFields(
                                fieldWithPath("content").description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").description("comment id"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("createdAt").description("댓글 생성시간")
                        )
                ));
    }

    @Test
    @DisplayName("댓글은 수정되어야한다.")
    void commentEdit() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        Long commentId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);

        CommentRequest commentRequest = new CommentRequest("수정 댓글 입니다.");
        CommentResponse commentResponse =
                new CommentResponse(1L, commentRequest.content(), LocalDateTime.now());
        given(editCommentUsecase.execute(userId, commentId, commentRequest)).willReturn(commentResponse);

        String json = objectMapper.writeValueAsString(commentRequest);

        mockMvc.perform(put("/api/post/comment/{commentId}", commentId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("commentId").description("댓글 수정 할 댓글 id")
                        ),
                        requestFields(
                                fieldWithPath("content").description("수정 할 댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").description("comment id"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("createdAt").description("댓글 생성시간")
                        )
                ));
    }

    @Test
    @DisplayName("답글은 등록되어야한다.")
    void registerReply() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        Long commentId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);

        ReplyRequest replyRequest = new ReplyRequest("답글 입니다.");
        ReplyResponse replyResponse = new ReplyResponse(1L, replyRequest.content(), LocalDateTime.now());

        given(createReplyUsecase.execute(userId, commentId, replyRequest)).willReturn(replyResponse);

        String json = objectMapper.writeValueAsString(replyRequest);

        mockMvc.perform(post("/api/post/comment/{commentId}/reply", commentId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("commentId").description("답글 등록 할 댓글 id")
                        ),
                        requestFields(
                                fieldWithPath("content").description("답글 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").description("reply id"),
                                fieldWithPath("content").description("답글 내용"),
                                fieldWithPath("createdAt").description("답글 생성시간")
                        )
                ));
    }

    @Test
    @DisplayName("답글은 수정되어야한다.")
    void editReply() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        Long replyId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);

        ReplyRequest replyRequest = new ReplyRequest("수정 답글 입니다.");
        ReplyResponse replyResponse = new ReplyResponse(1L, replyRequest.content(), LocalDateTime.now());

        given(editReplyUsecase.execute(userId, replyId, replyRequest)).willReturn(replyResponse);

        String json = objectMapper.writeValueAsString(replyRequest);

        mockMvc.perform(put("/api/post/comment/reply/{replyId}", replyId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("replyId").description("수정 할 답글 id")
                        ),
                        requestFields(
                                fieldWithPath("content").description("수정 할 답글 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").description("reply id"),
                                fieldWithPath("content").description("답글 내용"),
                                fieldWithPath("createdAt").description("답글 생성시간")
                        )
                ));
    }

    @Test
    @DisplayName("글의 좋아요는 완료되어야 한다.")
    void likePost() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        Long postId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);

        given(createPostLikeUsecase.execute(userId, postId)).willReturn(new PostLikeResponse(postId));

        mockMvc.perform(post("/api/post/{postId}/like", postId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("postId").description("좋아요 할 글 id")
                        ),
                        responseFields(
                                fieldWithPath("postId").description("좋아요 한 post id")
                        )
                ));
    }

    @Test
    @DisplayName("글의 좋아요 취소는 완료되어야 한다.")
    void dislikePost() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        Long postId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);

        given(destroyPostLikeUsecase.execute(userId, postId)).willReturn(new PostLikeResponse(postId));

        mockMvc.perform(post("/api/post/{postId}/dislike", postId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("postId").description("좋아요 취소 할 글 id")
                        ),
                        responseFields(
                                fieldWithPath("postId").description("좋아요 취소 한 post id")
                        )
                ));
    }

    @Test
    @DisplayName("글 삭제는 완료되어야 한다.")
    void deletePost() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        Long postId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);


        mockMvc.perform(delete("/api/post/{postId}", postId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("postId").description("삭제 할 글 id")
                        )
                ));
        verify(destroyPostUsecase).execute(userId, postId);
    }

    @Test
    @DisplayName("댓글 삭제는 완료되어야 한다.")
    void deleteComment() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        Long commentId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);

        mockMvc.perform(delete("/api/post/comment/{commentId}", commentId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("commentId").description("삭제 할 댓글 id")
                        )
                ));
        verify(destroyCommentUsecase).execute(userId, commentId);
    }

    @Test
    @DisplayName("답글 삭제는 완료되어야 한다.")
    void deleteReply() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        Long replyId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);

        mockMvc.perform(delete("/api/post/reply/{replyId}", replyId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("replyId").description("삭제 할 답글 id")
                        )
                ));
        verify(destroyReplyUsecase).execute(userId, replyId);
    }
}