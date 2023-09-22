package com.codesquad.secondhand.domain.chat.dto.response;

import com.codesquad.secondhand.domain.chat.entity.ChatRoom;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomProductResponse {
	private Long id;
	private String thumbnailUrl;

	public static ChatRoomProductResponse from(ChatRoom chatRoom) {
		return ChatRoomProductResponse.builder()
			.id(chatRoom.getProduct().getId())
			.thumbnailUrl(chatRoom.getProduct().getThumbnailImage())
			.build();
	}
}
