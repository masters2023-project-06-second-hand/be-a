package com.codesquad.secondhand.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.domain.chat.entity.ChatMember;
import com.codesquad.secondhand.domain.chat.entity.ChatRoom;
import com.codesquad.secondhand.domain.member.entity.Member;

public interface ChatMemberJpaRepository extends JpaRepository<ChatMember, Long> {
	Optional<ChatMember> findByMemberAndChatRoom(Member member, ChatRoom chatRoom);
}
