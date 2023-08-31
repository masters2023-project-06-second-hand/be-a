package com.codesquad.secondhand.common.util;

import javax.servlet.http.HttpServletRequest;

public class RequestParser {

	public static final String MEMBER_ID = "memberId";
	public static final String AUTHORIZATION = "Authorization";

	public static String extractAccessToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		return authorizationHeader.substring(7);
	}

	public static Long extractMemberId(HttpServletRequest request) {
		return (Long)request.getAttribute(MEMBER_ID);
	}
}
