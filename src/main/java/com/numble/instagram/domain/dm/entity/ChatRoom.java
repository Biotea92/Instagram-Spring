package com.numble.instagram.domain.dm.entity;

import com.numble.instagram.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user2;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public ChatRoom(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public void addMessage(Message newMessage) {
        this.messages.add(newMessage);
    }

    public boolean isChatRoomUser(User user) {
        return this.user1 == user || this.user2 == user;
    }
}
