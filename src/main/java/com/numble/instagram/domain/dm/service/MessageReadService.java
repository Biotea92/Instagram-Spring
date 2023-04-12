package com.numble.instagram.domain.dm.service;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.dm.entity.Message;
import com.numble.instagram.domain.dm.repository.MessageRepository;
import com.numble.instagram.support.paging.CursorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageReadService {

    private final MessageRepository messageRepository;

    public List<Message> getMessages(ChatRoom chatRoom, CursorRequest cursorRequest) {
        return messageRepository.findAllByLessThanIdAndChatRoom(chatRoom, cursorRequest);
    }
}
