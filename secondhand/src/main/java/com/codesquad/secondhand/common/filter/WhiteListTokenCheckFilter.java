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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class WhiteListTokenCheckFilter extends CommonFilter {

	public WhiteListTokenCheckFilter(JwtProvider jwtProvider, ObjectMapper objectMapper) {
		super(jwtProvider, objectMapper);
	}

	/**
	 * case1. header 에 token 이 존재하지만 유효하지 않을때 -> MalformedJwtException
	 * case2. header 에 Authorzation key 는 존재하지만 value 가 7자리 미만일때 -> StringIndexOutOfBoundsException
	 * case3. header 에 Authorzation key 자체가 존재하지 않을때 -> NullPointerException
	 * <p>
	 * case 1 인 경우에는 토큰이 비정상 적이라 판단하여 error response 를 보낸다.
	 * case 2,3 인 경우 해당 회원을 비회원으로 처리하여 role 을 부여한다.
	 *
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		if (whiteListCheck(request)) {
			try {
				String jwt = extractAccessToken(request);
				Claims claims = jwtProvider.getClaims(jwt);
				Object emailObj = claims.get("memberId");
				request.setAttribute("memberId", emailObj);
			} catch (StringIndexOutOfBoundsException | NullPointerException e) {
				request.setAttribute("role", "guest");
			} catch (MalformedJwtException e) {
				sendJwtExceptionResponse(response, e);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}
}
