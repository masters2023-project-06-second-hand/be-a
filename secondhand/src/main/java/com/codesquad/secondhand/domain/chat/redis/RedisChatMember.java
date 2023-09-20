package com.codesquad.secondhand.domain.chat.redis;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash(value = "RedisChatMember")
public class RedisChatMember {

	@Id
	private String id;

	@Indexed
	private Long chatRoomId;

	@Indexed
	private Long memberId;

	@Builder
	public RedisChatMember(Long chatRoomId, Long memberId) {
		this.chatRoomId = chatRoomId;
		this.memberId = memberId;
	}

	public static RedisChatMember of(Long chatRoomId, Long memberId) {
		return RedisChatMember.builder()
			.chatRoomId(chatRoomId)
			.memberId(memberId)
			.build();
	}
}
