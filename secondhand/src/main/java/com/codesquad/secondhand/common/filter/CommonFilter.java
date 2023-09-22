package com.codesquad.secondhand.common.filter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.codesquad.secondhand.domain.jwt.domain.JwtProvider;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.CustomException;
import com.codesquad.secondhand.exception.errorcode.JwtException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 기본적으로 filter 의 순서는
 * corsFilter -> JwtFilter -> signupTokenFilter -> whiteListTokenCheckFilter 순이다.
 */
@Component
public abstract class CommonFilter extends OncePerRequestFilter {

	private static final List<WhiteListUri> whiteListUris = List.of(
		new WhiteListUri("^/api/products/[0-9]*$", Set.of("GET")),
		new WhiteListUri("^/api/products/stat$", Set.of("GET")),
		new WhiteListUri("^/api/products$", Set.of("GET")),
		new WhiteListUri("^/api/categories$", Set.of("GET")),
		new WhiteListUri("^/api/oauth2/token$", Set.of("POST")),
		new WhiteListUri("^/api/regions$", Set.of("GET")),
		new WhiteListUri("^/ws$", Set.of("GET")),
		// whiteList 임시로 추가, header에 accessToken 포함 가능하면 그때 해제하면 된다.
		new WhiteListUri("^/connect/[0-9]*$", Set.of("GET")),
		new WhiteListUri("^/oauth2/authorization/[^/]+$", Set.of("GET")),
		new WhiteListUri("^/login/oauth2/code/.*$", Set.of("GET")),
		new WhiteListUri("^/api/regions$", Set.of("GET")),
		new WhiteListUri("^/api/products/[0-9]*/stat$", Set.of("GET"))
	);

	protected JwtProvider jwtProvider;
	protected ObjectMapper objectMapper;

	@Autowired
	public CommonFilter(JwtProvider jwtProvider, ObjectMapper objectMapper) {
		this.jwtProvider = jwtProvider;
		this.objectMapper = objectMapper;
	}

	protected boolean whiteListCheck(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String method = request.getMethod();

		return whiteListUris.stream()
			.anyMatch(entry -> entry.matches(uri, method));
	}

	protected boolean isSignupRequest(HttpServletRequest request) {
		return "/api/members/signup".equals(request.getRequestURI());
	}

	protected void sendJwtExceptionResponse(ServletResponse response, RuntimeException e) throws IOException {
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
