package com.numble.instagram.domain.dm.repository;

import com.numble.instagram.domain.dm.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom{
}
