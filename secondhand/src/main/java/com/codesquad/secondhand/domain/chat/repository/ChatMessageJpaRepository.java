package com.codesquad.secondhand.domain.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codesquad.secondhand.domain.chat.entity.ChatMessage;
import com.codesquad.secondhand.domain.chat.entity.ChatRoom;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {
	@Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom = :chatRoom AND cm.isRead = false")
	List<ChatMessage> findUnreadMessagesByChatRoom(@Param("chatRoom") ChatRoom chatRoom);

	List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom);
}
