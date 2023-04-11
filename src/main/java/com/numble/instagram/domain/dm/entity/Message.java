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

    @Column(updatable = false)
    private LocalDateTime sentAt;

    @PrePersist
    private void onPrePersist() {
        this.sentAt = LocalDateTime.now();
    }

    @Builder
    public Message(String content, User fromUser, User toUser) {
        this.content = content;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
