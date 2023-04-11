package com.numble.instagram.domain.dm.entity;

import com.numble.instagram.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Message {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private User toUser;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @Column(updatable = false)
    private LocalDateTime sentAt;

    @PrePersist
    private void onPrePersist() {
        this.sentAt = LocalDateTime.now();
    }

    @Builder
    public Message(String content, User fromUser, User toUser, ChatRoom chatRoom) {
        this.content = content;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.messageType = MessageType.TEXT;
        this.chatRoom = chatRoom;
    }

    public static Message create(String content, User fromUser, User toUser, ChatRoom chatRoom) {
        Message newMessage = Message.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .chatRoom(chatRoom)
                .content(content)
                .build();
        chatRoom.addMessage(newMessage);
        return newMessage;
    }
}
