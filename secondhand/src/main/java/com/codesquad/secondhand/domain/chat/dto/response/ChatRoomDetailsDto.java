package com.codesquad.secondhand.domain.chat.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

/**
 * 채팅방 들어가기 클릭시 사용되는 응답 DTO 이다.
 */
@Getter
@Builder
public class ChatRoomDetailsDto {
	private ChatProductResponse product;
	private String opponentName;
	private Long chatRoomId;
	private List<ChatMessageResponse> messages;

	public static ChatRoomDetailsDto of(ChatProductResponse product, String opponentName, Long chatRoomId,
		List<ChatMessageResponse> messages) {
		return ChatRoomDetailsDto.builder()
			.product(product)
			.opponentName(opponentName)
			.chatRoomId(chatRoomId)
			.messages(messages)
			.build();
	}
}
