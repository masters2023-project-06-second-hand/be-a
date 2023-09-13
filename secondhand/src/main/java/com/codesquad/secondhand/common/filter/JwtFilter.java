package com.codesquad.secondhand.common.filter;

import static com.codesquad.secondhand.common.util.RequestParser.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codesquad.secondhand.domain.jwt.domain.JwtProvider;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.JwtException;
import com.codesquad.secondhand.redis.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;

@Component
public class JwtFilter extends CommonFilter {
	private static final String TOKEN_PREFIX = "Bearer ";
	private static final String HEADER_AUTHORIZATION = "Authorization";
	private static final String MEMBER_ID = "memberId";
	public static final String OPTIONS = "OPTIONS";
	private RedisUtil redisUtil;

	@Autowired
	public JwtFilter(JwtProvider jwtProvider, ObjectMapper objectMapper, RedisUtil redisUtil) {
		super(jwtProvider, objectMapper);
		this.redisUtil = redisUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		//todo 나중에 배포하고 이게 꼭 필요한지 체크해보자
		if (request.getMethod().equals(OPTIONS)) {
			return;
		}

		if (whiteListCheck(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		if (!isContainToken(request)) {
			sendJwtExceptionResponse(response, new CustomRuntimeException(JwtException.MISSING_HEADER_TOKEN));
			return;
		}

		if (isSignupRequest(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		if (redisUtil.hasKeyBlackList(extractAccessToken(request))) {
			sendJwtExceptionResponse(response, new CustomRuntimeException(JwtException.BLACKLISTED_JWT_EXCEPTION));
			return;
		}

		try {
			handleRequest(request);
			filterChain.doFilter(request, response);
		} catch (RuntimeException e) {
			sendJwtExceptionResponse(response, e);
		}

	}

	private void handleRequest(HttpServletRequest request) {
		String token = extractAccessToken(request);
		Claims claims = jwtProvider.getClaims(token);
		Object memberIdObj = claims.get(MEMBER_ID);
		if (memberIdObj == null) {
			throw new CustomRuntimeException(JwtException.MALFORMED_JWT_EXCEPTION);
		}
		request.setAttribute(MEMBER_ID, memberIdObj);
	}

	private boolean isContainToken(HttpServletRequest request) {
		String authorization = request.getHeader(HEADER_AUTHORIZATION);
		return authorization != null && authorization.startsWith(TOKEN_PREFIX);
	}
}
