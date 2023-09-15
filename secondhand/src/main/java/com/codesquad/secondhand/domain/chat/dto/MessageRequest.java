package com.codesquad.secondhand.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
	private Long chatRoomId;
	private String message;
	private Long senderId;
	private Long productId;

}
