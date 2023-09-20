package com.codesquad.secondhand.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.codesquad.secondhand.common.interceptor.StompInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfigurer implements WebSocketMessageBrokerConfigurer {

	private final StompInterceptor stompInterceptor;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 해당 url 을 구독하는 구독자에게 메세지를 보낸다.
		registry.enableSimpleBroker("/sub");

		// 해당 url 로 시작하는 메세지만 Broker 에서 받아 처리해준다.
		registry.setApplicationDestinationPrefixes("/pub");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
			.setAllowedOrigins("*");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompInterceptor);
	}
}
