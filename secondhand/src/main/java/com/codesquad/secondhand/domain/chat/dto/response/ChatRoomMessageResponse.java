package com.codesquad.secondhand.domain.chat.dto.response;

import java.time.LocalDateTime;

import com.codesquad.secondhand.domain.chat.entity.ChatMessage;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class ChatRoomMessageResponse {
	private LocalDateTime sendAt;
	private String message;
	private Long unreadMessageCount;

	public ChatRoomMessageResponse(LocalDateTime sendAt, String message, Long unreadMessageCount) {
		this.sendAt = sendAt;
		this.message = message;
		this.unreadMessageCount = unreadMessageCount;
	}

	public static ChatRoomMessageResponse of(ChatMessage chatMessage, Long count) {
		return ChatRoomMessageResponse.builder()
			.sendAt(chatMessage.getSendAt())
			.message(chatMessage.getMessage())
			.unreadMessageCount(count)
			.build();
	}
}
