package com.codesquad.secondhand.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.domain.chat.entity.ChatMember;

public interface ChatMemberJpaRepository extends JpaRepository<ChatMember, Long> {
}
