package com.codesquad.secondhand.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.domain.chat.entity.ChatMessage;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {
}
