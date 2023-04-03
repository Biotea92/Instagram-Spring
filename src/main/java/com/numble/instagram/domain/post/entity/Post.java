package com.numble.instagram.domain.post.entity;

import com.numble.instagram.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String postImageUrl;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writerUser;

    private Long likeCount;

    @Column(updatable = false)
    private LocalDate createdDate;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void onPrePersist() {
        this.likeCount = 0L;
        this.createdDate = LocalDate.now();
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public Post(String postImageUrl, String content, User writerUser) {
        this.postImageUrl = postImageUrl;
        this.content = content;
        this.writerUser = writerUser;
    }
}
