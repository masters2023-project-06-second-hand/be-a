package com.codesquad.secondhand.domain.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.chat.entity.ChatMessage;
import com.codesquad.secondhand.domain.chat.entity.ChatRoom;
import com.codesquad.secondhand.domain.chat.redis.RedisChatMember;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RedisChatMemberService {
	private final RedisChatMemberQueryService redisChatMemberQueryService;
	private final ChatQueryService chatQueryService;

	// 채팅방 생성
	@Transactional
	public void connectChatRoom(Long chatRoomId, Long memberId) {
		RedisChatMember redisChatMember = RedisChatMember.of(chatRoomId, memberId);
		redisChatMemberQueryService.save(redisChatMember);
	}

	// 읽지 않은 메시지 채팅장 입장시 읽음 처리
	@Transactional
	public void updateReadMessage(Long chatRoomId, Long memberId) {
		ChatRoom chatRoom = chatQueryService.findChatRoomByChatRoomId(chatRoomId);

		// 채팅방에 존재하는 읽지않은 모든 메세지들을 가져온다.
		List<ChatMessage> chatMessages = chatQueryService.findUnreadMessagesByChatRoom(chatRoom);

		// chatMessages 중 상대방이 보낸 message 에 isRead 값을 모두 true 로 바꾼다.
		chatMessages.stream()
			.filter(chatMessage -> chatMessage.isSentByOpponent(memberId))
			.forEach(ChatMessage::updateReadStatusToTrue);
	}

	@Transactional
	public void deleteChatMember(Long chatRoomId, Long memberId) {
		redisChatMemberQueryService.deleteByChatRoomIdAndMemberId(chatRoomId, memberId);
	}
}
