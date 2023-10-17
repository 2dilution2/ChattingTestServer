package com.chat.chattingtest2.domain.crew.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.chattingtest2.domain.crew.model.entity.CrewMessage;

public interface CrewMessageRepository extends JpaRepository<CrewMessage, Long> {
}
