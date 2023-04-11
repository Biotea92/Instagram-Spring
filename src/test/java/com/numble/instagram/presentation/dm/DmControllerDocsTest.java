package com.numble.instagram.presentation.dm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.instagram.application.auth.token.TokenProvider;
import com.numble.instagram.application.usecase.dm.CreateMessageUsecase;
import com.numble.instagram.dto.request.dm.MessageRequest;
import com.numble.instagram.dto.response.dm.MessageResponse;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
}