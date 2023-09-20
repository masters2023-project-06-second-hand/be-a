package com.codesquad.secondhand.domain.chat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.chat.dto.request.ChatRequest;
import com.codesquad.secondhand.domain.chat.dto.request.MessageRequest;
import com.codesquad.secondhand.domain.chat.dto.response.ChatMessageResponse;
import com.codesquad.secondhand.domain.chat.dto.response.ChatProductResponse;
import com.codesquad.secondhand.domain.chat.dto.response.ChatRoomDetailsResponse;
import com.codesquad.secondhand.domain.chat.entity.ChatMessage;
import com.codesquad.secondhand.domain.chat.entity.ChatRoom;
import com.codesquad.secondhand.domain.chat.redis.RedisChatMember;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberQueryService;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.product.service.ProductQueryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChatService {

	private final RedisChatMemberQueryService redisChatMemberQueryService;
	private final MemberQueryService memberQueryService;
	private final ProductQueryService productQueryService;
	private final ChatQueryService chatQueryService;

	@Transactional
	public void sendMessage(MessageRequest messageRequest) {
		//1. chatRoomId 가 존재하는지 검증
		ChatRoom chatRoom = chatQueryService.findChatRoomByChatRoomId(messageRequest.getChatRoomId());

		//2. chatMessage DB에 저장
		Member member = memberQueryService.findById(messageRequest.getSenderId());
		ChatMessage chatMessage = chatQueryService.saveChatMessage(
			ChatMessage.of(messageRequest.getMessage(), chatRoom, member));

		//3. 채팅방에 다른 멤버가 없다면 상대방에게 알림 보내기
		List<RedisChatMember> redisChatMembers = redisChatMemberQueryService.findByChatRoomId(
			messageRequest.getChatRoomId());
		if (redisChatMembers.size() == 2) {
			// 메세지의 읽음 상태를 true 로 변경 (채팅방에 user 가 2명이기 때문에)
			chatMessage.updateReadStatusToTrue();
			//알림 x
			return;
		}
		//todo SSE 알림 보내기
	}

	@Transactional
	public ChatRoomDetailsResponse getChatRoom(ChatRequest chatRequest, Long memberId) {
		//1. chatRoom 있는지 없는지 확인 / 없다면 chatRoom 생성후 response 보내기
		Product product = productQueryService.findById(chatRequest.getProductId());
		Member sender = memberQueryService.findById(memberId);

		ChatRoom chatRoom = chatQueryService.findOrSaveChatRoom(memberId, product, sender);

		//2. 메세지 및 그외 데이터 가져오기
		List<ChatMessage> messages = chatQueryService.findAllChatMessageByChatRoom(chatRoom);

		List<ChatMessageResponse> messagesList = messages.stream()
			.map(ChatMessageResponse::of)
			.collect(Collectors.toList());

		ChatProductResponse chatProductResponse = ChatProductResponse.from(product);

		return ChatRoomDetailsResponse.of(chatProductResponse, product, chatRoom, messagesList);
	}
}
