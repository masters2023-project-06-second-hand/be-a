package com.codesquad.secondhand.domain.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codesquad.secondhand.domain.chat.entity.ChatMessage;
import com.codesquad.secondhand.domain.chat.entity.ChatRoom;
import com.codesquad.secondhand.domain.member.entity.Member;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {
	@Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom = :chatRoom AND cm.isRead = false")
	List<ChatMessage> findUnreadMessagesByChatRoom(@Param("chatRoom") ChatRoom chatRoom);

	List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom);

	@Query("SELECT COUNT(cm) FROM ChatMessage cm WHERE cm.chatRoom = :chatRoom AND cm.member = :opponent AND cm.isRead = false")
	Long countUnreadMessages(@Param("opponent") Member opponent, @Param("chatRoom") ChatRoom chatRoom);
}
