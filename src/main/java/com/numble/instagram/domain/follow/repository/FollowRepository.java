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

    @Query("SELECT f FROM Follow f JOIN f.toUser fu JOIN f.fromUser tu WHERE fu.id = :userId")
    List<Follow> findByToUser(@Param("userId") Long userId);

    @Query("SELECT f FROM Follow f JOIN f.fromUser fu JOIN f.toUser tu WHERE fu.id = :userId")
    List<Follow> findByFromUser(@Param("userId") Long userId);
}
