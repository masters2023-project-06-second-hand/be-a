package com.codesquad.secondhand.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomOpponentResponse {
	private String name;
	private String thumbnailUrl;

	public static ChatRoomOpponentResponse of(String name, String thumbnailUrl) {
		return ChatRoomOpponentResponse.builder()
			.name(name)
			.thumbnailUrl(thumbnailUrl)
			.build();
	}
}
