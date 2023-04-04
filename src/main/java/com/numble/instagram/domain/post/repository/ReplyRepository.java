package com.numble.instagram.domain.post.repository;

import com.numble.instagram.domain.post.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
