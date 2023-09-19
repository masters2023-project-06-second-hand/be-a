package com.codesquad.secondhand.domain.chat.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.codesquad.secondhand.domain.chat.redis.ChatMember;

public interface ChatMemberRedisRepository extends CrudRepository<ChatMember, String> {
	List<ChatMember> findByChatRoomId(Long chatRoomId);
}
