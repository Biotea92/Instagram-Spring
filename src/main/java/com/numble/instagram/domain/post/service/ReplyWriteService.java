package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Reply;
import com.numble.instagram.domain.post.repository.ReplyRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.exception.badrequest.NotReplyWriterException;
import com.numble.instagram.exception.notfound.ReplyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyWriteService {

    private final ReplyRepository replyRepository;

    public Reply register(User user, Comment comment, String content) {
        Reply newReply = Reply.builder()
                .replyWriteUser(user)
                .comment(comment)
                .content(content).build();
        comment.addReply(newReply);
        return replyRepository.save(newReply);
    }

    public Reply edit(User user, Long replyId, String content) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(ReplyNotFoundException::new);
        checkReplyWriter(user, reply);
        reply.updateContent(content);
        return reply;
    }

    private static void checkReplyWriter(User user, Reply reply) {
        if (!reply.isReplyWriteUser(user)) {
            throw new NotReplyWriterException();
        }
    }
}
