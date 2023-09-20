package com.codesquad.secondhand.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.domain.chat.entity.ChatRoom;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
	Optional<ChatRoom> findByMemberIdAndProductId(Long memberId, Long productId);
}
