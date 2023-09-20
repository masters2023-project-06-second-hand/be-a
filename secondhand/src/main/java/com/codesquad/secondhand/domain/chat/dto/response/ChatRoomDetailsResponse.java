package com.codesquad.secondhand.domain.chat.dto.response;

import java.util.List;

import com.codesquad.secondhand.domain.chat.entity.ChatRoom;
import com.codesquad.secondhand.domain.product.entity.Product;

import lombok.Builder;
import lombok.Getter;

/**
 * 채팅방 들어가기 클릭시 사용되는 응답 DTO 이다.
 */
@Getter
@Builder
public class ChatRoomDetailsResponse {
	private ChatProductResponse product;
	private String opponentName;
	private Long chatRoomId;
	private List<ChatMessageResponse> messages;

	public static ChatRoomDetailsResponse of(ChatProductResponse chatProductResponse, Product product,
		ChatRoom chatRoom,
		List<ChatMessageResponse> messages) {
		return ChatRoomDetailsResponse.builder()
			.product(chatProductResponse)
			.opponentName(product.getMember().getNickname())
			.chatRoomId(chatRoom.getId())
			.messages(messages)
			.build();
	}
}
