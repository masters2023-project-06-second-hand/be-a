package com.codesquad.secondhand.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.domain.chat.entity.ChatRoom;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
}
