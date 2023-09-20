package com.codesquad.secondhand.domain.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.chat.redis.RedisChatMember;
import com.codesquad.secondhand.domain.chat.repository.ChatMemberRedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisChatMemberQueryService {
	private final ChatMemberRedisRepository chatMemberRedisRepository;

	public void save(RedisChatMember redisChatMember) {
		chatMemberRedisRepository.save(redisChatMember);
	}

	public void deleteByChatRoomIdAndMemberId(Long chatRoomId, Long memberId) {
		chatMemberRedisRepository.deleteByChatRoomIdAndMemberId(chatRoomId, memberId);
	}

	public List<RedisChatMember> findByChatRoomId(Long chatRoomId) {
		return chatMemberRedisRepository.findByChatRoomId(chatRoomId);
	}
}
