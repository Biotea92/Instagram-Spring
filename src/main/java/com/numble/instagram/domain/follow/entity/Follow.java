package com.numble.instagram.domain.follow.entity;

import com.numble.instagram.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Follow {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime followedAt;

    @PrePersist
    private void onPrePersist() {
        this.followedAt = LocalDateTime.now();
    }

    @Builder
    public Follow(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public static Follow create(User fromUser, User toUser) {
        return Follow.builder().fromUser(fromUser).toUser(toUser).build();
    }
}
