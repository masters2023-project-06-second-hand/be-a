package com.codesquad.secondhand.domain.chat.dto.response;

import java.time.LocalDateTime;

import com.codesquad.secondhand.domain.chat.entity.ChatMessage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomMessageResponse {
	private LocalDateTime sendAt;
	private String message;
	private Long unreadMessageCount;

	public static ChatRoomMessageResponse of(ChatMessage chatMessage, Long count) {
		return ChatRoomMessageResponse.builder()
			.sendAt(chatMessage.getSendAt())
			.message(chatMessage.getMessage())
			.unreadMessageCount(count)
			.build();
	}
}
