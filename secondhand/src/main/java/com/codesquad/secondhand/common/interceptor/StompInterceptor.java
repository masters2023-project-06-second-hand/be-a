package com.codesquad.secondhand.common.interceptor;

import java.util.Objects;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.codesquad.secondhand.domain.chat.service.ChatService;
import com.codesquad.secondhand.domain.jwt.domain.JwtProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompInterceptor implements ChannelInterceptor {

	private final JwtProvider jwtProvider;
	private final ChatService chatService;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		log.info("StompAccessor = {}", accessor);

		handleMessage(accessor.getCommand(), accessor);
		return message;
	}

	private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor) {
		switch (stompCommand) {

			case CONNECT:
				log.info("CONNECT !!");
				Long memberId = validateAccessToken(accessor);
				connectToChatRoom(accessor, memberId);
				break;
			case SUBSCRIBE:
				log.info("SUBSCRIBE !!");
				break;
			case SEND:
				// validateAccessToken(accessor);
				break;
			case DISCONNECT:
				log.info("DISCONNECT !!");
				memberId = validateAccessToken(accessor);
				chatService.deleteChatMember(getChatRoomId(accessor), memberId);
				break;
		}
	}

	private void connectToChatRoom(StompHeaderAccessor accessor, Long memberId) {
		// 채팅방 번호를 가져온다.
		Long chatRoomId = getChatRoomId(accessor);

		//채팅방 입장 처리 -> Redis에 입장 내역 저장
		chatService.connectChatRoom(chatRoomId, memberId);

		//읽지 않은 채팅을 전부 읽음 처리
		chatService.updateReadMessage(chatRoomId, memberId);

		//todo 메세지를 보낸 상대방의 1을 없애는 로직이 필요함
	}

	//todo chatRoomId 어떻게 발급할지 얘기해봐
	private Long getChatRoomId(StompHeaderAccessor accessor) {
		return
			Long.valueOf(
				Objects.requireNonNull(
					accessor.getFirstNativeHeader("chatRoomId")
				));
	}

	//todo bearer 뺴고 가져오도록 아니면 로직을 수정해야할듯
	private String getAccessToken(StompHeaderAccessor accessor) {
		return accessor.getFirstNativeHeader("Authorization");
	}

	private Long validateAccessToken(StompHeaderAccessor accessor) {
		try {
			String token = getAccessToken(accessor);
			return jwtProvider.getClaims(token).get("memberId", Long.class);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
				 IllegalArgumentException ex) {
			//todo exception 던지기
			throw new IllegalStateException();
		}
	}

}
