package com.numble.instagram.presentation.dm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.instagram.application.auth.token.TokenProvider;
import com.numble.instagram.application.usecase.dm.CreateMessageUsecase;
import com.numble.instagram.application.usecase.dm.GetChatRoomWithMessagesUsecase;
import com.numble.instagram.application.usecase.dm.GetChatRoomsUsecase;
import com.numble.instagram.domain.dm.entity.MessageType;
import com.numble.instagram.dto.request.dm.MessageRequest;
import com.numble.instagram.dto.response.dm.ChatRoomWithMessageResponse;
import com.numble.instagram.dto.response.dm.MessageDetailResponse;
import com.numble.instagram.dto.response.dm.MessageResponse;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.LongStream;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
class DmControllerDocsTest {

    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private CreateMessageUsecase createMessageUsecase;
    @MockBean
    private GetChatRoomWithMessagesUsecase getChatRoomWithMessagesUsecase;
    @MockBean
    private GetChatRoomsUsecase getChatRoomsUsecase;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private static final String DOCUMENT_IDENTIFIER = "dm/{method-name}/";

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
    @DisplayName("dm은 전송된다.")
    void sendMessage() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long fromUserId = 1L;
        Long toUserId = 2L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(fromUserId);

        MessageRequest messageRequest = new MessageRequest("새로운 메시지입니다.");
        MessageResponse messageResponse = new MessageResponse(toUserId, messageRequest.content());

        given(createMessageUsecase.execute(fromUserId, toUserId, messageRequest)).willReturn(messageResponse);

        String json = objectMapper.writeValueAsString(messageRequest);

        mockMvc.perform(post("/api/dm/{toUserId}", toUserId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("toUserId").description("dm 전송할 user Id")
                        ),
                        requestFields(
                                fieldWithPath("content").description("메시지 내용")
                        ),
                        responseFields(
                                fieldWithPath("userId").description("전송한 user Id"),
                                fieldWithPath("content").description("전송 된 메시지 내용")
                        )
                ));
    }

    @Test
    @DisplayName("채팅방의 메시지 내역은 조회된다.")
    void getChatRoomWithMessages() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        Long chatroomId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);

        CursorRequest cursorRequest = new CursorRequest(10L, 5);

        ArrayList<MessageDetailResponse> messageDetailResponses = new ArrayList<>(LongStream.range(1L, 6L).mapToObj(i ->
                new MessageDetailResponse(
                        i,
                        userId,
                        "from_user_nickname",
                        "https://fromUserProfile.png",
                        2L,
                        "to_user_nickname",
                        "https://toUserProfile.png",
                        "메시지 내용" + i,
                        MessageType.TEXT.name(),
                        LocalDateTime.now().minusDays(6L - i))).toList());
        Collections.reverse(messageDetailResponses);

        PageCursor<MessageDetailResponse> pageCursor = new PageCursor<>(new CursorRequest(1L, 5), messageDetailResponses);

        given(getChatRoomWithMessagesUsecase.execute(userId, chatroomId, cursorRequest))
                .willReturn(pageCursor);

        mockMvc.perform(get("/api/dm/chatroom/{chatroomId}", chatroomId)
                        .param("key", String.valueOf(cursorRequest.key()))
                        .param("size", String.valueOf(cursorRequest.size()))
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("chatroomId").description("조회할 채팅방 Id")
                        ),
                        queryParameters(
                                parameterWithName("key").description("가져 올 커서의 key"),
                                parameterWithName("size").description("가져올 message의 size")
                        ),
                        responseFields(
                                fieldWithPath("nextCursorRequest").description("다음 커서 정보"),
                                fieldWithPath("nextCursorRequest.key").description("다음 커서 key"),
                                fieldWithPath("nextCursorRequest.size").description("message size"),
                                fieldWithPath("body").description("message 정보"),
                                fieldWithPath("body[].id").description("message Id"),
                                fieldWithPath("body[].fromUserId").description("보낸 User Id"),
                                fieldWithPath("body[].fromUserNickname").description("보낸 User 닉네임"),
                                fieldWithPath("body[].fromUserProfileUrl").description("보낸 User 이미지 Url"),
                                fieldWithPath("body[].toUserId").description("받은 User Id"),
                                fieldWithPath("body[].toUserNickname").description("받은 User 닉네임"),
                                fieldWithPath("body[].toUserProfileUrl").description("받은 User 이미지 Url"),
                                fieldWithPath("body[].content").description("message 내용"),
                                fieldWithPath("body[].messageType").description("message 타입"),
                                fieldWithPath("body[].sentAt").description("message 보낸 시간")
                        )
                ));
    }

    @Test
    @DisplayName("DM 목록은 채팅방을 기준으로 lastMessage와 함께 조회된다.")
    void getChatRoom() throws Exception {
        String authorizationHeader = "Bearer access-token";
        Long userId = 1L;
        given(tokenProvider.isValidToken(authorizationHeader)).willReturn(true);
        given(tokenProvider.getUserId(authorizationHeader)).willReturn(userId);

        CursorRequest cursorRequest = new CursorRequest(10L, 5);

        ArrayList<ChatRoomWithMessageResponse> chatRoomWithMessageResponses = new ArrayList<>(LongStream.range(1L, 6L).mapToObj(i ->
                new ChatRoomWithMessageResponse(
                        i,
                        "nickname" + i,
                        "https://profileUrl.png",
                        "채팅방 마지막 메시지 내용" + i,
                        LocalDateTime.now().minusDays(6L - i))).toList());
        Collections.reverse(chatRoomWithMessageResponses);

        PageCursor<ChatRoomWithMessageResponse> pageCursor =
                new PageCursor<>(new CursorRequest(1L, 5), chatRoomWithMessageResponses);

        given(getChatRoomsUsecase.execute(userId, cursorRequest))
                .willReturn(pageCursor);

        mockMvc.perform(get("/api/dm/chatroom")
                        .param("key", String.valueOf(cursorRequest.key()))
                        .param("size", String.valueOf(cursorRequest.size()))
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        queryParameters(
                                parameterWithName("key").description("가져 올 커서의 key"),
                                parameterWithName("size").description("가져올 chatRoom의 size")
                        ),
                        responseFields(
                                fieldWithPath("nextCursorRequest").description("다음 커서 정보"),
                                fieldWithPath("nextCursorRequest.key").description("다음 커서 key"),
                                fieldWithPath("nextCursorRequest.size").description("chatroom size"),
                                fieldWithPath("body").description("chatroom 정보"),
                                fieldWithPath("body[].chatroomId").description("chatroom id"),
                                fieldWithPath("body[].nickname").description("마지막 메시지 보낸 사람 닉네임"),
                                fieldWithPath("body[].profileImageUrl").description("마지막 메시지 보낸 사람 프로필 URL"),
                                fieldWithPath("body[].lastMessageContent").description("마지막 메시지 내용"),
                                fieldWithPath("body[].lastSentAt").description("마지막 메시지 보낸 시간")
                        )
                ));
    }
}