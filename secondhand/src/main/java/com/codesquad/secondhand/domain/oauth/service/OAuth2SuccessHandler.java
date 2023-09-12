package com.codesquad.secondhand.domain.oauth.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.jwt.domain.Jwt;
import com.codesquad.secondhand.domain.jwt.service.JwtService;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberQueryService;
import com.codesquad.secondhand.domain.oauth.domain.OAuthAttributes;
import com.codesquad.secondhand.domain.oauth.domain.UserProfile;
import com.codesquad.secondhand.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	public static final boolean IS_SIGN_IN = true;
	private final MemberQueryService memberQueryService;
	private final JwtService jwtService;

	@Override
	@Transactional
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken)authentication;
		String email = extractEmailFromToken(oauthToken);

		Optional<Member> memberFromDb = memberQueryService.findByEmail(email);

		if (memberFromDb.isPresent()) {
			handleExistingMember(response, memberFromDb.get());
		} else {
			handleNewMember(response, email);
		}
	}

	private String extractEmailFromToken(OAuth2AuthenticationToken oauthToken) {
		String registrationId = oauthToken.getAuthorizedClientRegistrationId();
		OAuth2User oAuth2User = oauthToken.getPrincipal();
		UserProfile userProfile = OAuthAttributes.extract(registrationId, oAuth2User.getAttributes());

		log.debug("email : {}", userProfile.getEmail());
		return userProfile.getEmail();
	}

	private void handleExistingMember(HttpServletResponse response, Member member) throws
		IOException {
		Jwt jwt = jwtService.createTokens(member.getId(), IS_SIGN_IN);
		sendMemberSuccessResponse(response, jwt, member);
	}

	private void handleNewMember(HttpServletResponse response, String email) throws IOException {
		Jwt jwt = jwtService.createSignUpToken(email);
		sendMemberNotFoundResponse(response, jwt);
	}

	private void sendMemberNotFoundResponse(HttpServletResponse response, Jwt jwt) throws IOException {
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

	private void sendMemberSuccessResponse(HttpServletResponse response, Jwt jwt, Member member) throws IOException {
		Map<String, String> messageMap = new HashMap<>();
		messageMap.put("accessToken", jwt.getAccessToken());
		messageMap.put("refreshToken", jwt.getRefreshToken());
		messageMap.put("memberId", String.valueOf(member.getId()));

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpStatus.OK.value());
		PrintWriter out = response.getWriter();
		out.print(new ObjectMapper().writeValueAsString(messageMap));
		out.flush();
	}
}
