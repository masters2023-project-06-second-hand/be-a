package com.codesquad.secondhand.domain.chat.service;

import org.springframework.stereotype.Service;

import com.codesquad.secondhand.domain.chat.dto.MessageRequest;
import com.codesquad.secondhand.domain.chat.entity.ChatMember;
import com.codesquad.secondhand.domain.chat.entity.ChatMessage;
import com.codesquad.secondhand.domain.chat.entity.ChatRoom;
import com.codesquad.secondhand.domain.chat.repository.ChatMemberJpaRepository;
import com.codesquad.secondhand.domain.chat.repository.ChatMessageJpaRepository;
import com.codesquad.secondhand.domain.chat.repository.ChatRoomJpaRepository;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberQueryService;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.product.service.ProductQueryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {

	private final ChatMemberJpaRepository chatMemberJpaRepository;
	private final ChatMessageJpaRepository chatMessageJpaRepository;
	private final ChatRoomJpaRepository chatRoomJpaRepository;
	private final ProductQueryService productQueryService;
	private final MemberQueryService memberQueryService;

	public void sendMessage(MessageRequest messageRequest) {
		Product product = productQueryService.findById(messageRequest.getProductId());
		ChatRoom chatRoom = getChatRoom(messageRequest, product);

		Member sendMember = memberQueryService.findById(messageRequest.getSenderId());
		Member productOwner = memberQueryService.findById(product.getMember().getId());

		if (sendMember.getId() != productOwner.getId()) {
			findOrCreateChatMember(sendMember, chatRoom);
		}
		findOrCreateChatMember(productOwner, chatRoom);

		ChatMessage chatMessage = ChatMessage.of(messageRequest.getMessage(), chatRoom, sendMember);

		chatMessageJpaRepository.save(chatMessage);
		// 5. 채팅방에 다른 멤버가 있는데 해당 멤버가 접속중이지 않다면 알림
		// 6. 마지막 메세지 기록
	}

	private ChatRoom getChatRoom(MessageRequest messageRequest, Product product) {
		if (messageRequest.getChatRoomId() == null) {
			ChatRoom chatRoom = ChatRoom.from(product);
			return chatRoomJpaRepository.save(chatRoom);
		}
		return chatRoomJpaRepository.findById(messageRequest.getChatRoomId()).orElseThrow();
	}

	private ChatMember findOrCreateChatMember(Member member, ChatRoom chatRoom) {
		return chatMemberJpaRepository.findByMemberAndChatRoom(member, chatRoom)
			.orElse(chatMemberJpaRepository.save(ChatMember.of(member, chatRoom)));
	}

}
