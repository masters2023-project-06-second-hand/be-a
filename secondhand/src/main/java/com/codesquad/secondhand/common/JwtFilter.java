package com.codesquad.secondhand.common;

import static com.codesquad.secondhand.common.util.RequestParser.*;

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
import com.codesquad.secondhand.exception.errorcode.CustomException;
import com.codesquad.secondhand.exception.errorcode.JwtException;
import com.codesquad.secondhand.redis.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtFilter implements Filter {
	private static final String TOKEN_PREFIX = "Bearer ";
	private static final String HEADER_AUTHORIZATION = "Authorization";
	private static final String MEMBER_ID = "memberId";
	private static final String[] whiteListUris = {
		"/"
	};
	public static final String OPTIONS = "OPTIONS";
	private final JwtProvider jwtProvider;
	private final ObjectMapper objectMapper;
	private final RedisUtil redisUtil;

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
			sendJwtExceptionResponse(response, new CustomRuntimeException(JwtException.MISSING_HEADER_TOKEN));
			return;
		}

		if (redisUtil.hasKeyBlackList(extractAccessToken(httpServletRequest))) {
			sendJwtExceptionResponse(response, new CustomRuntimeException(JwtException.BLACKLISTED_JWT_EXCEPTION));
			return;
		}

		// 회원가입 토큰 핸들링
		if (isSignupRequest(httpServletRequest)) {
			try {
				handleSignupRequest(httpServletRequest);
				chain.doFilter(request, response);
				//return 을 하는 이유는 return 하지 않으면 chain.doFilter를 호출하더라도 filter의 이후 로직이 실행된다.
				return;
			} catch (RuntimeException e) {
				sendJwtExceptionResponse(response, e);
				return;
			}
		}

		try {
			handleRequest(request, httpServletRequest);
			chain.doFilter(request, response);
		} catch (RuntimeException e) {
			sendJwtExceptionResponse(response, e);
		}
	}

	private void handleSignupRequest(HttpServletRequest request) {
		String token = extractAccessToken(request);
		Claims claims = jwtProvider.getClaimsFromSignUpToken(token);
		Object emailObj = claims.get("email");
		if (emailObj == null) {
			throw new CustomRuntimeException(JwtException.MALFORMED_SIGN_UP_TOKEN);
		}
		request.setAttribute("email", emailObj);
	}

	private void handleRequest(ServletRequest request, HttpServletRequest httpServletRequest) {
		String token = extractAccessToken(httpServletRequest);
		Claims claims = jwtProvider.getClaims(token);
		Object memberIdObj = claims.get(MEMBER_ID);
		if (memberIdObj == null) {
			throw new CustomRuntimeException(JwtException.MALFORMED_JWT_EXCEPTION);
		}
		request.setAttribute(MEMBER_ID, memberIdObj);
	}

	private boolean whiteListCheck(String uri) {
		return PatternMatchUtils.simpleMatch(whiteListUris, uri);
	}

	private boolean isContainToken(HttpServletRequest request) {
		String authorization = request.getHeader(HEADER_AUTHORIZATION);
		return authorization != null && authorization.startsWith(TOKEN_PREFIX);
	}

	private boolean isSignupRequest(HttpServletRequest request) {
		return "/api/members/signup".equals(request.getRequestURI());
	}

	private void sendJwtExceptionResponse(ServletResponse response, RuntimeException e) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		((HttpServletResponse)response).setStatus(HttpStatus.UNAUTHORIZED.value());

		CustomException jwtException = JwtException.from(e);
		response.getWriter().write(
			objectMapper.writeValueAsString(
				new CustomRuntimeException(jwtException).sendError().getBody()
			));
	}
}
