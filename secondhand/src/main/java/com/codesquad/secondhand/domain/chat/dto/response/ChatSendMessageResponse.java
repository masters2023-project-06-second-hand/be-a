package com.codesquad.secondhand.domain.chat.dto.response;

import com.codesquad.secondhand.domain.chat.entity.ChatMessage;
import com.codesquad.secondhand.domain.chat.entity.ChatRoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChatSendMessageResponse {

	private Long chatRoomId;
	private ChatMessageResponse messages;

	public static ChatSendMessageResponse of(ChatMessage chatMessage, ChatRoom chatRoom) {
		return ChatSendMessageResponse.builder()
			.messages(ChatMessageResponse.of(chatMessage))
			.chatRoomId(chatRoom.getId())
			.build();
	}
}
