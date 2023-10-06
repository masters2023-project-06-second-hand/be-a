package com.codesquad.secondhand.domain.chat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.chat.entity.ChatMessage;
import com.codesquad.secondhand.domain.chat.entity.ChatRoom;
import com.codesquad.secondhand.domain.chat.repository.ChatMessageJpaRepository;
import com.codesquad.secondhand.domain.chat.repository.ChatMessageQueryRepository;
import com.codesquad.secondhand.domain.chat.repository.ChatRoomJpaRepository;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.ChatException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryService {

	private final ChatRoomJpaRepository chatRoomJpaRepository;
	private final ChatMessageJpaRepository chatMessageJpaRepository;
	private final ChatMessageQueryRepository chatMessageQueryRepository;

	/**
	 * DB에 특정 상품과 구매자에 대한 ChatRoom 이 이미 생성되어 있다면 찾고, 없다면 ChatRoom 을 만들어주는 메서드 이다.
	 *
	 * @param participantId
	 * @param product
	 * @param participant
	 * @return
	 */
	public ChatRoom findOrSaveChatRoom(Long participantId, Product product, Member participant) {
		return chatRoomJpaRepository.findByMemberIdAndProductId(participantId, product.getId())
			.orElseGet(() -> chatRoomJpaRepository.save(ChatRoom.of(product, participant)));
	}

	public ChatRoom findChatRoomByChatRoomId(Long chatRoomId) {
		return chatRoomJpaRepository.findById(chatRoomId)
			.orElseThrow(() -> new CustomRuntimeException(ChatException.INVALID_CHAT_ROOM_ID));
	}

	public List<ChatMessage> findAllChatMessageByChatRoom(ChatRoom chatRoom) {
		return chatMessageJpaRepository.findAllByChatRoom(chatRoom);
	}

	public ChatMessage saveChatMessage(ChatMessage chatMessage) {
		return chatMessageJpaRepository.save(chatMessage);
	}

	public List<ChatRoom> findAllChatRoomByMember(Long memberId) {
		return chatRoomJpaRepository.findAllByMemberId(memberId);
	}

	public Long getUnReadCount(Member opponent, ChatRoom chatRoom) {
		return chatMessageJpaRepository.countUnreadMessages(opponent, chatRoom);
	}

	public Optional<ChatMessage> findLastMessage(ChatRoom chatRoom) {
		return chatMessageQueryRepository.findLatestMessage(chatRoom);
	}

	public Long countByProduct(Product product) {
		return chatRoomJpaRepository.countByProduct(product);
	}
}
