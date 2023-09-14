package com.codesquad.secondhand.common.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

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
				validateAccessToken(accessor);
				break;
			case SUBSCRIBE:
				log.info("SUBSCRIBE !!");
			case SEND:
				break;
		}
	}

	private String getAccessToken(StompHeaderAccessor accessor) {
		return accessor.getFirstNativeHeader("Authorization");
	}

	private void validateAccessToken(StompHeaderAccessor accessor) {
		try {
			String token = getAccessToken(accessor);
			jwtProvider.getClaims(token);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
				 IllegalArgumentException ex) {
			// throw new IllegalStateException(JwtException.from(ex).getMessage());
		}
	}

}
