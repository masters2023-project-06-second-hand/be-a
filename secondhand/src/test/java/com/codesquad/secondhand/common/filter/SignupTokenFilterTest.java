package com.codesquad.secondhand.common.filter;

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
import org.springframework.test.context.ActiveProfiles;

import com.codesquad.secondhand.domain.jwt.domain.Jwt;
import com.codesquad.secondhand.domain.jwt.domain.JwtProvider;
import com.codesquad.secondhand.exception.errorcode.JwtException;

@SpringBootTest
@ActiveProfiles("test")
class SignupTokenFilterTest {

	public static final long MEMBER_ID = 1L;
	public static final String TEST_EMAIL = "admin@gmail.com";

	@Autowired
	SignupTokenFilter signupTokenFilter;

	@Autowired
	JwtProvider jwtProvider;

	@Mock
	HttpServletRequest request;

	@Mock
	HttpServletResponse response;

	@Mock
	FilterChain filterChain;

	@Test
	@DisplayName("httpServletRequest 의 uri가 회원가입 uri 일때, signUpToken의 claim에 email이 없다면 예외가 발생한다.")
	public void doFilterWithInvalidSignUpToken() throws Exception {
		// given
		given(request.getMethod()).willReturn("POST");
		given(request.getRequestURI()).willReturn("/api/members/signup");

		// 예외 응답을 처리하기위한 writer 설정
		StringWriter stringWriter = getStringWriter();

		//accessToken 생성
		Jwt jwt = jwtProvider.createSignUpToken(Map.of("memberId", MEMBER_ID));
		given(request.getHeader("Authorization")).willReturn("Bearer " + jwt.getSignUpToken());

		// when
		signupTokenFilter.doFilterInternal(request, response, filterChain);

		// then
		String responseBody = stringWriter.toString();
		Assertions.assertThat(responseBody).contains(JwtException.MALFORMED_SIGN_UP_TOKEN.getErrorMessage());
	}

	@Test
	@DisplayName("httpServletRequest 의 uri가 회원가입 uri 일때, signUpToken의 claim에 email 있다면 다음 필터가 실행된다.")
	public void doFilterWithValidSignUpToken() throws Exception {
		// given
		given(request.getMethod()).willReturn("POST");
		given(request.getRequestURI()).willReturn("/api/members/signup");

		//accessToken 생성
		Jwt jwt = jwtProvider.createSignUpToken(Map.of("memberId", MEMBER_ID, "email", TEST_EMAIL));
		given(request.getHeader("Authorization")).willReturn("Bearer " + jwt.getSignUpToken());

		// when
		signupTokenFilter.doFilterInternal(request, response, filterChain);

		// then
		verify(filterChain, times(1)).doFilter(request, response);
	}

	private StringWriter getStringWriter() throws IOException {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		given(response.getWriter()).willReturn(printWriter);
		return stringWriter;
	}

}
