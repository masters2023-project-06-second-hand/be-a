package com.codesquad.secondhand.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public class RequestParser {

	public static final String MEMBER_ID = "memberId";
	public static final String AUTHORIZATION = "Authorization";
	public static final String EMAIL = "email";

	public static String extractAccessToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		return authorizationHeader.substring(7);
	}

	public static Long extractMemberId(HttpServletRequest request) {
		return Long.valueOf(String.valueOf(request.getAttribute(MEMBER_ID)));
	}

	public static String extractEmail(HttpServletRequest request) {
		Object email = request.getAttribute(EMAIL);
		return email != null ? email.toString() : null;
	}

	public static String extractAccessTokenFromAccessor(StompHeaderAccessor accessor) {
		String token = accessor.getFirstNativeHeader(AUTHORIZATION);
		return token.substring(7);
	}
}
