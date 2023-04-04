package com.numble.instagram.application.usecase.post;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Reply;
import com.numble.instagram.domain.post.service.CommentReadService;
import com.numble.instagram.domain.post.service.ReplyWriteService;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.service.UserReadService;
import com.numble.instagram.dto.request.post.ReplyRequest;
import com.numble.instagram.dto.response.post.ReplyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateReplyUsecase {

    private final UserReadService userReadService;
    private final CommentReadService commentReadService;
    private final ReplyWriteService replyWriteService;

    public ReplyResponse execute(Long userId, Long commentId, ReplyRequest replyRequest) {
        User user = userReadService.getUser(userId);
        Comment comment = commentReadService.getComment(commentId);
        Reply newReply = replyWriteService.register(user, comment, replyRequest.content());
        return ReplyResponse.from(newReply);
    }
}
