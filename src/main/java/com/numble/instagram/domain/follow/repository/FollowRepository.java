package com.numble.instagram.domain.follow.repository;

import com.numble.instagram.domain.follow.entity.Follow;
import com.numble.instagram.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {

    Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);
}
