package com.codesquad.secondhand.common.filter;

import static com.codesquad.secondhand.common.util.RequestParser.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.codesquad.secondhand.domain.jwt.domain.JwtProvider;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.JwtException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;

@Component
public class SignupTokenFilter extends CommonFilter {

	public SignupTokenFilter(JwtProvider jwtProvider, ObjectMapper objectMapper) {
		super(jwtProvider, objectMapper);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		if (isSignupRequest(request)) {
			try {
				handleSignupRequest(request);
			} catch (RuntimeException e) {
				sendJwtExceptionResponse(response, e);
				return;
			}
		}

		filterChain.doFilter(request, response);
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
}
