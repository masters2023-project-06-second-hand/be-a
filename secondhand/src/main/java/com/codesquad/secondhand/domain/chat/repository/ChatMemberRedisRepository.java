package com.codesquad.secondhand.domain.chat.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.codesquad.secondhand.domain.chat.redis.RedisChatMember;

public interface ChatMemberRedisRepository extends CrudRepository<RedisChatMember, String> {
	List<RedisChatMember> findByChatRoomId(Long chatRoomId);

	void deleteByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);
}
