package com.numble.instagram.domain.follow.repository;

import com.numble.instagram.domain.follow.entity.Follow;
import com.numble.instagram.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {

    Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);

    @Query("SELECT f FROM Follow f JOIN FETCH f.fromUser WHERE f.toUser.id = :userId")
    List<Follow> findByToUserIdWithFromUser(@Param("userId") Long userId);

    @Query("SELECT f FROM Follow f JOIN FETCH f.toUser WHERE f.fromUser.id = :userId")
    List<Follow> findByFromUserIdWithToUser(@Param("userId") Long userId);
}
