package com.codesquad.secondhand.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomListResponse {
	private Long id;
	private ChatRoomProductResponse product;
	private ChatRoomOpponentResponse opponent;
	private ChatRoomMessageResponse message;

	public static ChatRoomListResponse of(Long chatRoomId, ChatRoomProductResponse chatRoomProduct,
		ChatRoomOpponentResponse chatRoomOpponent, ChatRoomMessageResponse chatRoomMessage) {
		return ChatRoomListResponse.builder()
			.id(chatRoomId)
			.product(chatRoomProduct)
			.opponent(chatRoomOpponent)
			.message(chatRoomMessage)
			.build();
	}
}
