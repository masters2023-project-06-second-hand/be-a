package com.codesquad.secondhand.domain.oauth.service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// DefaultOAuth2UserService 는 OAuth2.0 제공자로부터 사용자의 정보를 가져오는 서비스다.
		OAuth2UserService delegate = new DefaultOAuth2UserService();

		// OAuth 서비스(google, naver)에서 가져온 유저 정보를 담고있음 (loadUser 는 사용자 정보를 가져오는 메서드)
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		Map<String, Object> attributes = oAuth2User.getAttributes();

		// OAuth 로그인 시 키(pk)가 되는 값
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
			.getUserInfoEndpoint().getUserNameAttributeName();

		return new DefaultOAuth2User(null, attributes, userNameAttributeName);
	}
}
