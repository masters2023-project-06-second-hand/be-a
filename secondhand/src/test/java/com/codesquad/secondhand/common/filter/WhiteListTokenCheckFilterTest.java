package com.codesquad.secondhand.common.filter;

import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.codesquad.secondhand.domain.jwt.domain.Jwt;
import com.codesquad.secondhand.domain.jwt.domain.JwtProvider;
import com.codesquad.secondhand.exception.errorcode.JwtException;

@SpringBootTest
@ActiveProfiles("test")
class WhiteListTokenCheckFilterTest {

	public static final long MEMBER_ID = 1L;

	@Autowired
	WhiteListTokenCheckFilter whiteListTokenCheckFilter;

	@Autowired
	JwtProvider jwtProvider;

	@Mock
	HttpServletRequest request;

	@Mock
	HttpServletResponse response;

	@Mock
	FilterChain filterChain;

	@Test
	@DisplayName("요청 uri 가 whiteListUri 일때, 헤더에 토큰이 없다면 request 의 attribute에 key가 role 인 값을 포함하여 다음 필터가 실행된다.")
	void doFilterInternalWithNoToken() throws ServletException, IOException {
		// given
		given(request.getMethod()).willReturn("GET");
		given(request.getRequestURI()).willReturn("/api/products");

		//accessToken 생성
		given(request.getHeader("Authorization")).willReturn(null);

		// when
		whiteListTokenCheckFilter.doFilterInternal(request, response, filterChain);

		// then
		verify(filterChain, times(1)).doFilter(request, response);
		verify(request, times(1)).setAttribute("role", "guest");
	}

	@Test
	@DisplayName("요청 uri 가 whiteListUri 일때, 토큰이 있다면 request 의 attribute에 key가 role 인 값 없이 다음 필터가 실행된다.")
	void doFilterInternalsWithToken() throws ServletException, IOException {
		// given
		given(request.getMethod()).willReturn("GET");
		given(request.getRequestURI()).willReturn("/api/products");

		//accessToken 생성
		Jwt jwt = jwtProvider.createTokens(Map.of("memberId", MEMBER_ID));
		given(request.getHeader("Authorization")).willReturn("Bearer " + jwt.getAccessToken());

		// when
		whiteListTokenCheckFilter.doFilterInternal(request, response, filterChain);

		// then
		verify(filterChain, times(1)).doFilter(request, response);
		verify(request, times(0)).setAttribute("role", "guest");
	}

	@Test
	@DisplayName("요청 uri 가 whiteListUri 일때, 유효하지 않은 토큰이 있다면 예외 응답이 전송된다.")
	void doFilterInternalsWithInvalidToken() throws ServletException, IOException {
		// given
		given(request.getMethod()).willReturn("GET");
		given(request.getRequestURI()).willReturn("/api/products");

		StringWriter stringWriter = getStringWriter();

		//잘못된토큰 생성
		given(request.getHeader("Authorization")).willReturn("Bearer " + "aaabbcccddd");

		// when
		whiteListTokenCheckFilter.doFilterInternal(request, response, filterChain);

		// then
		String responseBody = stringWriter.toString();
		verify(filterChain, times(0)).doFilter(request, response);
		verify(request, times(0)).setAttribute("role", "guest");
		Assertions.assertThat(responseBody).contains(JwtException.MALFORMED_JWT_EXCEPTION.getErrorMessage());
	}

	private StringWriter getStringWriter() throws IOException {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		given(response.getWriter()).willReturn(printWriter);
		return stringWriter;
	}
}
