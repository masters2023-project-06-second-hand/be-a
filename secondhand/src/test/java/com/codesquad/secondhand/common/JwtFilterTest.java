package com.codesquad.secondhand.common;

import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codesquad.secondhand.domain.jwt.Jwt;
import com.codesquad.secondhand.domain.jwt.JwtProvider;
import com.codesquad.secondhand.exception.errorcode.JwtException;
import com.codesquad.secondhand.redis.util.RedisUtil;

@SpringBootTest
class JwtFilterTest {

	public static final long MEMBER_ID = 1L;
	public static final String TEST_EMAIL = "admin@gmail.com";
	@Autowired
	JwtFilter jwtFilter;

	@Autowired
	RedisUtil redisUtil;

	@Autowired
	JwtProvider jwtProvider;

	@Mock
	HttpServletRequest request;

	@Mock
	HttpServletResponse response;

	@Mock
	FilterChain filterChain;

	@Test
	@DisplayName("httpServletRequest 의 method 가 Options 면 아무런 동작없이 return 한다.")
	public void doFilterWithOptionMethod() throws Exception {
		// given
		given(request.getMethod()).willReturn(JwtFilter.OPTIONS);

		// when & then
		jwtFilter.doFilter(request, response, filterChain);

		// then
		verify(request, times(1)).getMethod();
		verifyNoMoreInteractions(request);
	}

	@Test
	@DisplayName("httpServletRequest 의 uri 가 whiteListUris 에 포함된다면 token 검사를 수행하지 않는다.")
	public void doFilterWithWhiteLabelUri() throws Exception {
		// given
		given(request.getMethod()).willReturn("GET");
		given(request.getRequestURI()).willReturn("/");

		// when
		jwtFilter.doFilter(request, response, filterChain);

		// then
		verify(request, times(1)).getMethod();
		verify(request, times(1)).getRequestURI();
		verifyNoMoreInteractions(request);
	}

	@Test
	@DisplayName("httpServletRequest 의 메서드가 OPTIONS가 아니며, uri가 whiteListUris에 존재하지 않을때 header에 토큰이 없다면 예외가 발생한다.")
	public void doFilterWithNoToken() throws Exception {
		// given
		given(request.getMethod()).willReturn("GET");
		given(request.getRequestURI()).willReturn("/api");

		StringWriter stringWriter = getStringWriter();

		// when & then
		jwtFilter.doFilter(request, response, filterChain);
		String responseBody = stringWriter.toString();

		//then
		Assertions.assertThat(responseBody).contains(JwtException.MISSING_HEADER_TOKEN.getErrorMessage());
	}

	@Test
	@DisplayName("httpServletRequest header의 토큰이 blackList 토큰이라면 예외가 발생한다..")
	public void doFilterWithBlackListToken() throws Exception {
		// given
		given(request.getMethod()).willReturn("GET");
		given(request.getRequestURI()).willReturn("/api");

		// 예외 응답을 처리하기위한 writer 설정
		StringWriter stringWriter = getStringWriter();

		//blackList에 token 등록
		Jwt jwt = jwtProvider.createTokens(Map.of("memberId", MEMBER_ID));
		redisUtil.setBlackList(jwt.getAccessToken(), "accessToken", 60);

		given(request.getHeader("Authorization")).willReturn("Bearer " + jwt.getAccessToken());

		// when
		jwtFilter.doFilter(request, response, filterChain);
		String responseBody = stringWriter.toString();

		//then
		Assertions.assertThat(responseBody).contains(JwtException.BLACKLISTED_JWT_EXCEPTION.getErrorMessage());
	}

	@Test
	@DisplayName("httpServletRequest 의 uri가 회원가입 uri 이며, signUpToken이 존재하면 예외가 발생하지 않는다.")
	public void doFilterWithSignUpToken() throws Exception {
		// given
		given(request.getMethod()).willReturn("POST");
		given(request.getRequestURI()).willReturn("/api/members/signup");

		//signUpToken 생성
		Jwt jwt = jwtProvider.createSignUpToken(Map.of("memberId", MEMBER_ID, "email", TEST_EMAIL));
		given(request.getHeader("Authorization")).willReturn("Bearer " + jwt.getSignUpToken());

		// when
		jwtFilter.doFilter(request, response, filterChain);

		// then
		verify(filterChain, times(1)).doFilter(request, response);
	}

	@Test
	@DisplayName("httpServletRequest 의 uri가 회원가입 uri 일때, signUpToken의 claim에 email이 없다면 예외가 발생한다.")
	public void doFilterWithInvalidSignUpToken() throws Exception {
		// given
		given(request.getMethod()).willReturn("POST");
		given(request.getRequestURI()).willReturn("/api/members/signup");

		// 예외 응답을 처리하기위한 writer 설정
		StringWriter stringWriter = getStringWriter();

		//signUpToken 생성
		Jwt jwt = jwtProvider.createSignUpToken(Map.of("memberId", MEMBER_ID));
		given(request.getHeader("Authorization")).willReturn("Bearer " + jwt.getSignUpToken());

		// when
		jwtFilter.doFilter(request, response, filterChain);

		// then
		String responseBody = stringWriter.toString();
		verify(filterChain, times(0)).doFilter(request, response);
		Assertions.assertThat(responseBody).contains(JwtException.MALFORMED_SIGN_UP_TOKEN.getErrorMessage());
	}

	@Test
	@DisplayName("Memberid를 claim으로 가지고있는 AccessToken을 사용하면, JwtFilter를 통과한다.")
	public void doFilterWithAccessToken() throws Exception {
		// given
		given(request.getMethod()).willReturn("GET");
		given(request.getRequestURI()).willReturn("/api");

		//AccessToken 생성
		Jwt jwt = jwtProvider.createTokens(Map.of("memberId", MEMBER_ID));
		given(request.getHeader("Authorization")).willReturn("Bearer " + jwt.getAccessToken());

		// when
		jwtFilter.doFilter(request, response, filterChain);

		// then
		verify(filterChain, times(1)).doFilter(request, response);
	}

	@Test
	@DisplayName("Memberid를 claim으로 가지고있지 않은 AccessToken을 사용하면, 예외가 발생한다.")
	public void doFilterWithInvalidAccessToken() throws Exception {
		// given
		given(request.getMethod()).willReturn("GET");
		given(request.getRequestURI()).willReturn("/api");

		// 예외 응답을 처리하기위한 writer 설정
		StringWriter stringWriter = getStringWriter();

		//AccessToken 생성
		Jwt jwt = jwtProvider.createTokens(Map.of());
		given(request.getHeader("Authorization")).willReturn("Bearer " + jwt.getAccessToken());

		// when
		jwtFilter.doFilter(request, response, filterChain);

		// then
		String responseBody = stringWriter.toString();
		verify(filterChain, times(0)).doFilter(request, response);
		Assertions.assertThat(responseBody).contains(JwtException.MALFORMED_JWT_EXCEPTION.getErrorMessage());
	}

	private StringWriter getStringWriter() throws IOException {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		given(response.getWriter()).willReturn(printWriter);
		return stringWriter;
	}

}
