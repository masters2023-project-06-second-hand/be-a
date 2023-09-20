package com.codesquad.secondhand.domain.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.chat.redis.RedisChatMember;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RedisChatMemberService {
	private final RedisChatMemberQueryService redisChatMemberQueryService;

	// 채팅방 생성
	@Transactional
	public void connectChatRoom(Long chatRoomId, Long memberId) {
		RedisChatMember redisChatMember = RedisChatMember.of(chatRoomId, memberId);
		redisChatMemberQueryService.save(redisChatMember);
	}

	@Transactional
	public void deleteChatMember(Long chatRoomId, Long memberId) {
		redisChatMemberQueryService.deleteByChatRoomIdAndMemberId(chatRoomId, memberId);
	}
}
