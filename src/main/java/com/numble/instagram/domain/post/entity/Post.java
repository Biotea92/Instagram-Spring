package com.numble.instagram.domain.post.entity;

import com.numble.instagram.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
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

    @Lob
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User writeUser;

    private Long likeCount;

    @Column(updatable = false)
    private LocalDate createdDate;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Version
    private Long version;

    @PrePersist
    private void onPrePersist() {
        this.likeCount = 0L;
        this.createdDate = LocalDate.now();
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public Post(String postImageUrl, String content, User writeUser) {
        this.postImageUrl = postImageUrl;
        this.content = content;
        this.writeUser = writeUser;
    }

    public boolean isWriter(User user) {
        return this.writeUser == user;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public String updatePostImageUrl(String postImageUrl) {
        String willDeleteImageUrl = this.postImageUrl;
        this.postImageUrl = postImageUrl;
        return willDeleteImageUrl;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        this.likeCount--;
    }

    public void addComment(Comment comment) {
        this.getComments().add(comment);
    }
}
