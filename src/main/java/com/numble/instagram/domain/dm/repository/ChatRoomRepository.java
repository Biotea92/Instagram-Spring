package com.numble.instagram.domain.dm.repository;

import com.numble.instagram.domain.dm.entity.ChatRoom;
import com.numble.instagram.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("""
            SELECT cr
            FROM ChatRoom cr
            WHERE (cr.user1 = :user1 AND cr.user2 = :user2) OR (cr.user1 = :user2 AND cr.user2 = :user1)
            """)
    Optional<ChatRoom> findChatRoomByUsers(@Param("user1") User user1, @Param("user2") User user2);
}
