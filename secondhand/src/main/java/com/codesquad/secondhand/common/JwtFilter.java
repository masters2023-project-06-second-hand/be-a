package com.codesquad.secondhand.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import com.codesquad.secondhand.domain.jwt.JwtProvider;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.JwtException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtFilter implements Filter {
	private static final String TOKEN_PREFIX = "Bearer ";
	private static final String HEADER_AUTHORIZATION = "Authorization";
	private static final String MEMBER_ID = "memberId";
	private static final String[] whiteListUris = {
		"/api/members/signup",
		"/"
	};
	public static final String OPTIONS = "OPTIONS";

	private final JwtProvider jwtProvider;
	private final ObjectMapper objectMapper;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws ServletException, IOException {

		HttpServletRequest httpServletRequest = (HttpServletRequest)request;

		if (httpServletRequest.getMethod().equals(OPTIONS)) {
			return;
		}

		if (whiteListCheck(httpServletRequest.getRequestURI())) {
			chain.doFilter(request, response);
			return;
		}

		if (!isContainToken(httpServletRequest)) {
			sendJwtExceptionResponse(response, new MalformedJwtException(""));
			return;
		}

		try {
			String token = extractAccessToken(httpServletRequest);
			Claims claims = jwtProvider.getClaims(token);
			Long memberId = convertMemberIdToLong(claims);
			request.setAttribute(MEMBER_ID, memberId);
			chain.doFilter(request, response);
		} catch (RuntimeException e) {
			sendJwtExceptionResponse(response, e);
		}
	}

	public String extractAccessToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		return authorizationHeader.substring(7);
	}

	private Long convertMemberIdToLong(Claims claims) {
		Object memberIdObj = claims.get(MEMBER_ID);
		Long memberId = Long.valueOf(memberIdObj.toString());
		return memberId;
	}

	private boolean whiteListCheck(String uri) {
		return PatternMatchUtils.simpleMatch(whiteListUris, uri);
	}

	private boolean isContainToken(HttpServletRequest request) {
		String authorization = request.getHeader(HEADER_AUTHORIZATION);
		return authorization != null && authorization.startsWith(TOKEN_PREFIX);
	}

	private void sendJwtExceptionResponse(ServletResponse response, RuntimeException e) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		((HttpServletResponse)response).setStatus(HttpStatus.UNAUTHORIZED.value());

		JwtException jwtException = JwtException.from(e);

		response.getWriter().write(
			objectMapper.writeValueAsString(
				new CustomRuntimeException(jwtException).sendError().getBody()
			));
	}
}
