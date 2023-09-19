package com.codesquad.secondhand.domain.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.chat.dto.MessageRequest;
import com.codesquad.secondhand.domain.chat.entity.ChatMessage;
import com.codesquad.secondhand.domain.chat.entity.ChatRoom;
import com.codesquad.secondhand.domain.chat.redis.ChatMember;
import com.codesquad.secondhand.domain.chat.repository.ChatMemberRedisRepository;
import com.codesquad.secondhand.domain.chat.repository.ChatMessageJpaRepository;
import com.codesquad.secondhand.domain.chat.repository.ChatRoomJpaRepository;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberQueryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChatService {

	private final ChatMessageJpaRepository chatMessageJpaRepository;
	private final ChatRoomJpaRepository chatRoomJpaRepository;
	private final ChatMemberRedisRepository chatMemberRedisRepository;
	private final MemberQueryService memberQueryService;

	@Transactional
	public void sendMessage(MessageRequest messageRequest) {
		//1. chatRoomId 가 존재하는지 검증
		ChatRoom chatRoom = chatRoomJpaRepository.findById(messageRequest.getChatRoomId()).orElseThrow();

		//2. chatMessage DB에 저장
		Member member = memberQueryService.findById(messageRequest.getSenderId());
		ChatMessage chatMessage = chatMessageJpaRepository.save(
			ChatMessage.of(messageRequest.getMessage(), chatRoom, member));

		//3. 채팅방에 다른 멤버가 없다면 상대방에게 알림 보내기
		List<ChatMember> chatMembers = chatMemberRedisRepository.findByChatRoomId(messageRequest.getChatRoomId());
		if (chatMembers.size() == 2) {
			// 메세지의 읽음 상태를 true 로 변경 (채팅방에 user 가 2명이기 때문에)
			chatMessage.updateReadStatusToTrue();
			//알림 x
			return;
		}
		//todo SSE 알림 보내기
	}

	// 채팅방 생성
	@Transactional
	public void connectChatRoom(Long chatRoomId, Long memberId) {
		ChatMember chatMember = ChatMember.builder()
			.memberId(memberId)
			.chatRoomId(chatRoomId)
			.build();
		chatMemberRedisRepository.save(chatMember);
	}

	// 읽지 않은 메시지 채팅장 입장시 읽음 처리
	@Transactional
	public void updateReadMessage(Long chatRoomId, Long memberId) {
		//todo chatRoom websocket 연결전에 만들지 아니면 최초 연결 될떄 만들지 정하기
		ChatRoom chatRoom = chatRoomJpaRepository.findById(chatRoomId).orElseThrow();

		// 채팅방에 존재하는 읽지않은 모든 메세지들을 가져온다.
		List<ChatMessage> chatMessages = chatMessageJpaRepository.findUnreadMessagesByChatRoom(chatRoom);

		// chatMessages 중 상대방이 보낸 message 에 isRead 값을 모두 true 로 바꾼다.
		chatMessages.stream()
			.filter(chatMessage -> chatMessage.getMember().getId().equals(memberId))
			.forEach(chatMessage -> chatMessage.updateReadStatusToTrue());
	}

	@Transactional
	public void deleteChatMember(Long chatRoomId, Long memberId) {
		chatMemberRedisRepository.deleteByChatRoomIdAndMemberId(chatRoomId, memberId);
	}
}
