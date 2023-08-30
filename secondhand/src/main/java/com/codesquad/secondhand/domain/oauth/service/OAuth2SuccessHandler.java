package com.codesquad.secondhand.domain.oauth.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.codesquad.secondhand.domain.jwt.Jwt;
import com.codesquad.secondhand.domain.jwt.JwtProvider;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.repository.MemberJpaRepository;
import com.codesquad.secondhand.domain.oauth.domain.OAuthAttributes;
import com.codesquad.secondhand.domain.oauth.domain.UserProfile;
import com.codesquad.secondhand.domain.token.entity.Token;
import com.codesquad.secondhand.domain.token.repository.TokenJpaRepository;
import com.codesquad.secondhand.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final MemberJpaRepository memberJpaRepository;
	private final TokenJpaRepository tokenJpaRepository;
	private final JwtProvider jwtProvider;

	@Override
	@Transactional
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken)authentication;
		String email = extractEmailFromToken(oauthToken);

		Optional<Member> memberFromDb = memberJpaRepository.findByEmail(email);

		if (memberFromDb.isPresent()) {
			handleExistingMember(request, response, memberFromDb.get());
		} else {
			handleNewMember(response, email);
		}
	}

	private String extractEmailFromToken(OAuth2AuthenticationToken oauthToken) {
		String registrationId = oauthToken.getAuthorizedClientRegistrationId();
		OAuth2User oAuth2User = (OAuth2User)oauthToken.getPrincipal();
		UserProfile userProfile = OAuthAttributes.extract(registrationId, oAuth2User.getAttributes());

		log.debug("email : {}", userProfile.getEmail());
		return userProfile.getEmail();
	}

	private void handleExistingMember(HttpServletRequest request, HttpServletResponse response, Member member) throws
		IOException {
		Jwt jwt = jwtProvider.createTokens(Collections.singletonMap("memberId", member.getId()));

		Token token = Token.builder()
			.member(member)
			.refreshToken(jwt.getRefreshToken())
			.build();
		log.debug("{}", token);

		tokenJpaRepository.deleteByMemberId(member.getId());
		tokenJpaRepository.save(token);
		setResponseWithTokens(response, jwt);

		//todo 인디케이터로 할지 아니면 메인페이지로 할지
		String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/test")
			.build().toUriString();
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	private void setResponseWithTokens(HttpServletResponse response, Jwt jwt) {
		Cookie refreshTokenCookie = new Cookie("refreshToken", jwt.getRefreshToken());
		refreshTokenCookie.setPath("/");
		response.addCookie(refreshTokenCookie);
		response.setHeader("Authorization", "Bearer " + jwt.getAccessToken());
	}

	private void handleNewMember(HttpServletResponse response, String email) throws IOException {
		Jwt jwt = jwtProvider.createSignUpToken(Collections.singletonMap("email", email));
		sendErrorResponse(response, jwt);
	}

	private void sendErrorResponse(HttpServletResponse response, Jwt jwt) throws IOException {
		Map<String, String> messageMap = new HashMap<>();
		messageMap.put("signupToken", jwt.getSignUpToken());
		messageMap.put("error", "존재하지 않는 유저");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, messageMap);

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		PrintWriter out = response.getWriter();
		out.print(new ObjectMapper().writeValueAsString(errorResponse));
		out.flush();
	}
}
