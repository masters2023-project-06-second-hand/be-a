package com.codesquad.secondhand.common.filter;

import java.io.IOException;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
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

	protected static final String[] whiteListUris = {
		"/",
		"/api/products"
	};
	protected JwtProvider jwtProvider;
	protected ObjectMapper objectMapper;

	@Autowired
	public CommonFilter(JwtProvider jwtProvider, ObjectMapper objectMapper) {
		this.jwtProvider = jwtProvider;
		this.objectMapper = objectMapper;
	}

	/**
	 * 비회원은 전체 카테고리별 전체 상품 조회 요청이 가능하기 떄문에 해당 url 을 whiteList로 등록한다.
	 * 요청 url이 /api/products 인경우 HTTPMETHOD 가 GET 인 경우인지 한번더 확인했다.
	 * 위처럼 한 이유는 상품 수정의 url 또한 /api/products (PUT) 이기 떄문이다.
	 *
	 * @param request
	 * @return
	 */
	protected boolean whiteListCheck(HttpServletRequest request) {
		String uri = request.getRequestURI();
		boolean matches = PatternMatchUtils.simpleMatch(whiteListUris, uri);

		if (matches && uri.startsWith("/api/products")) {
			return request.getMethod().equals("GET");
		}
		return matches;
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
