package com.codesquad.secondhand.common.filter;

import static com.codesquad.secondhand.common.util.RequestParser.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.codesquad.secondhand.domain.jwt.domain.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WhiteListTokenCheckFilter extends CommonFilter {

	public WhiteListTokenCheckFilter(JwtProvider jwtProvider, ObjectMapper objectMapper) {
		super(jwtProvider, objectMapper);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		if (whiteListCheck(request)) {
			try {
				String jwt = extractAccessToken(request);
				jwtProvider.getClaims(jwt);
			} catch (RuntimeException e) {
				request.setAttribute("role", "guest");
			}
		}

		filterChain.doFilter(request, response);
	}
}
