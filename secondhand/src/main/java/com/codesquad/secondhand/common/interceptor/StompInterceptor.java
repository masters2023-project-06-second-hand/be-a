package com.codesquad.secondhand.common.interceptor;

import static com.codesquad.secondhand.common.util.RequestParser.*;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.codesquad.secondhand.domain.chat.service.ChatService;
import com.codesquad.secondhand.domain.jwt.domain.JwtProvider;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.JwtException;

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

	//todo chatRoomId 어떻게 발급할지 얘기해봐 (얘기 해보고 예외 처리하자)
	private Long getChatRoomId(StompHeaderAccessor accessor) {
		return Long.valueOf(accessor.getFirstNativeHeader("chatRoomId"));
	}

	private Long validateAccessToken(StompHeaderAccessor accessor) {
		try {
			String token = extractAccessTokenFromAccessor(accessor);
			return jwtProvider.getClaims(token).get("memberId", Long.class);
		} catch (RuntimeException e) {
			throw new CustomRuntimeException(JwtException.from(e));
		}
	}

}
