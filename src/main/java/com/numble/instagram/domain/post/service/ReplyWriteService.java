package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Comment;
import com.numble.instagram.domain.post.entity.Reply;
import com.numble.instagram.domain.post.repository.ReplyRepository;
import com.numble.instagram.domain.user.entity.User;
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
        return replyRepository.save(newReply);
    }
}
