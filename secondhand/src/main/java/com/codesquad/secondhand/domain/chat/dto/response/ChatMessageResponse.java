package com.codesquad.secondhand.domain.chat.dto.response;

import com.codesquad.secondhand.domain.chat.entity.ChatMessage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageResponse {
	private String message;
	private Long senderId;

	public static ChatMessageResponse of(ChatMessage message) {
		return ChatMessageResponse.builder()
			.message(message.getMessage())
			.senderId(message.getMember().getId())
			.build();
	}
}
