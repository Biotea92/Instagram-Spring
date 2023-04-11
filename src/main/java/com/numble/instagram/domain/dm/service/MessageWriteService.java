package com.numble.instagram.domain.dm.service;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.entity.Message;
import com.numble.instagram.domain.dm.repository.MessageRepository;
import com.numble.instagram.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageWriteService {

    private final MessageRepository messageRepository;

    public Message create(User fromUser, User toUser, ChatRoom chatRoom, String content) {
        Message newMessage = Message.create(content, fromUser, toUser, chatRoom);
        return messageRepository.save(newMessage);
    }
}
