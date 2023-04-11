package com.numble.instagram.presentation.post;

import com.numble.instagram.application.auth.token.TokenProvider;
import com.numble.instagram.application.usecase.post.GetFeedPostsUsecase;
import com.numble.instagram.dto.response.post.CommentDetailResponse;
import com.numble.instagram.dto.response.post.PostDetailResponse;
import com.numble.instagram.dto.response.post.ReplyDetailResponse;
import com.numble.instagram.support.paging.CursorRequest;
import com.numble.instagram.support.paging.PageCursor;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
class FeedControllerDocsTest {

    private MockMvc mockMvc;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private GetFeedPostsUsecase getFeedPostsUsecase;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private static final String DOCUMENT_IDENTIFIER = "feed/{method-name}/";

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
    @DisplayName("피드를 조회한다.")
    void getFeeds() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);

        CursorRequest cursorRequest = new CursorRequest(10L, 5);

        ReplyDetailResponse replyResponse = new ReplyDetailResponse(
                1L,
                "reply-content",
                LocalDateTime.now(),
                1L,
                "replyWriter",
                "https://profileImage.jpg"
        );

        List<CommentDetailResponse> comments = new ArrayList<>(LongStream.range(1L, 4L).mapToObj(i ->
                new CommentDetailResponse(i,
                        "comment-content" + i,
                        LocalDateTime.now(),
                        i,
                        "commentWriter_" + i,
                        "https://profileImage.png",
                        List.of(replyResponse))).toList());
        Collections.reverse(comments);

        List<PostDetailResponse> postDetailResponses = new ArrayList<>(LongStream.range(1L, 6L).mapToObj(i ->
                new PostDetailResponse(i,
                        "post-content" + i,
                        "https://postImage" + i + ".png",
                        0L,
                        LocalDateTime.now().minusDays(i),
                        LocalDate.now().minusDays(i),
                        userId,
                        "post-writer",
                        "https://profileImage.png",
                        true,
                        comments)).toList());
        Collections.reverse(postDetailResponses);

        PageCursor<PostDetailResponse> pageCursor = new PageCursor<>(new CursorRequest(1L, 5), postDetailResponses);

        given(getFeedPostsUsecase.execute(eq(userId), eq(cursorRequest))).willReturn(pageCursor);

        mockMvc.perform(get("/api/feed")
                        .param("key", String.valueOf(cursorRequest.key()))
                        .param("size", String.valueOf(cursorRequest.size()))
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        queryParameters(
                                parameterWithName("key").description("가져 올 커서의 key"),
                                parameterWithName("size").description("가져올 포스트의 size")
                        ),
                        responseFields(
                                fieldWithPath("nextCursorRequest").description("다음 커서 정보"),
                                fieldWithPath("nextCursorRequest.key").description("다음 커서 key"),
                                fieldWithPath("nextCursorRequest.size").description("페이지 size"),
                                fieldWithPath("body").description("게시글 정보"),
                                fieldWithPath("body[].postId").description("게시글 id"),
                                fieldWithPath("body[].content").description("게시글 내용"),
                                fieldWithPath("body[].postImageUrl").description("게시글 이미지 URL"),
                                fieldWithPath("body[].likeCount").description("게시글 좋아요 수"),
                                fieldWithPath("body[].createdAt").description("게시 날짜 시간"),
                                fieldWithPath("body[].createdDate").description("게시 날짜"),
                                fieldWithPath("body[].userId").description("게시글 작성자"),
                                fieldWithPath("body[].nickname").description("게시글 작성자 닉네임"),
                                fieldWithPath("body[].profileImageUrl").description("게시글 작성자 profile URL"),
                                fieldWithPath("body[].isPostLiked").description("작성자의 게시글 좋아요 여부"),
                                fieldWithPath("body[].comments").description("게시글의 댓글 정보"),
                                fieldWithPath("body[].comments[].commentId").description("댓글 id"),
                                fieldWithPath("body[].comments[].content").description("댓글 내용"),
                                fieldWithPath("body[].comments[].createdAt").description("댓글 단 날짜 시간"),
                                fieldWithPath("body[].comments[].userId").description("댓글 작성자 id"),
                                fieldWithPath("body[].comments[].nickname").description("댓글 작성자 닉네임"),
                                fieldWithPath("body[].comments[].profileImageUrl").description("댓글 작성자 profile URL"),
                                fieldWithPath("body[].comments[].replies").description("댓글의 답글 정보"),
                                fieldWithPath("body[].comments[].replies[].replyId").description("답글 id"),
                                fieldWithPath("body[].comments[].replies[].content").description("답글 내용"),
                                fieldWithPath("body[].comments[].replies[].createdAt").description("답글 단 날짜시간"),
                                fieldWithPath("body[].comments[].replies[].userId").description("답글 작성자 id"),
                                fieldWithPath("body[].comments[].replies[].nickname").description("답글 작성자 닉네임"),
                                fieldWithPath("body[].comments[].replies[].profileImageUrl").description("답글 작성자 profile URL")
                        )
                ));
    }
}