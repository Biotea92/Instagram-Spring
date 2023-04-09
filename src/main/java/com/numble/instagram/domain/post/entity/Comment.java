package com.numble.instagram.domain.post.entity;

import com.numble.instagram.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Lob
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User commentWriteUser;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "comment", fetch = LAZY, cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @PrePersist
    private void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public Comment(String content, Post post, User commentWriteUser) {
        this.content = content;
        this.post = post;
        this.commentWriteUser = commentWriteUser;
    }
    
    public boolean isCommentWriteUser(User user) {
        return this.commentWriteUser == user;
    }

    public void updateContent(String content) {
        this.content = this.content.equals(content) ? this.content : content;
    }

    public void addReply(Reply reply) {
        this.replies.add(reply);
    }
}
