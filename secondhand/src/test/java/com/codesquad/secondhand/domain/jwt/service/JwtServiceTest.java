package com.codesquad.secondhand.domain.jwt.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.codesquad.secondhand.annotation.ServiceIntegrationTest;
import com.codesquad.secondhand.domain.jwt.domain.Jwt;
import com.codesquad.secondhand.domain.jwt.domain.JwtProvider;
import com.codesquad.secondhand.domain.jwt.entity.Token;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.redis.util.RedisUtil;

import io.jsonwebtoken.Claims;

@ServiceIntegrationTest
class JwtServiceTest {

	@Autowired
	JwtProvider jwtProvider;

	@Autowired
	JwtService jwtService;

	@Autowired
	JwtQueryService jwtQueryService;

	@Autowired
	RedisUtil redisUtil;

	@Autowired
	RedisTemplate<String, Object> redisBlackListTemplate;

	@AfterEach
	void resetBlackList() {
		redisBlackListTemplate.delete(
			"test");
	}

	@DisplayName("엑세스 토큰을 레디스의 블랙리스트에 등록한다.")
	@Test
	void setBlackList() {
		// given
		String accessToken = "test";
		jwtService.setBlackList(accessToken);

		// when
		Boolean result = redisUtil.hasKeyBlackList("test");

		// then
		assertThat(result).isTrue();
	}

	@DisplayName("로그인 이력이 없을 때 토큰을 생성하고 리프레시 토큰을 DB에 저장한다.")
	@Test
	void createTokensIsSignFalse() {
		// given
		Long memberId = 1L;
		Boolean isSign = false;
		Jwt jwt = jwtService.createTokens(memberId, isSign);

		// when
		Token findToken = jwtQueryService.findByRefreshToken(jwt.getRefreshToken());

		// then
		assertThat(findToken.getMember().getId()).isEqualTo(memberId);
	}

	@DisplayName("로그인 이력이 있을 때 기존의 리프레시 토큰을 제거하고 새로운 리프레시 토큰을 DB에 저장한다.")
	@Test
	void createTokensIsSignTrue() {
		// given
		Long memberId = 1L;
		Boolean isSign = false;
		Jwt firstJwt = jwtService.createTokens(memberId, isSign);
		Jwt secondJwt = jwtService.createTokens(memberId, true);

		// when
		Token findToken = jwtQueryService.findByRefreshToken(secondJwt.getRefreshToken());

		// then
		assertThatThrownBy(() -> jwtQueryService.findByRefreshToken(firstJwt.getRefreshToken())).isInstanceOf(
			CustomRuntimeException.class);
		assertThat(secondJwt.getRefreshToken()).isEqualTo(findToken.getRefreshToken());
	}

	@DisplayName("signUp 토큰을 생성한다.")
	@Test
	void createSignUpToken() {
		// given
		String email = "test@test.com";

		// when
		Jwt jwt = jwtService.createSignUpToken(email);

		// then
		String actual = jwt.getSignUpToken();
		Claims claims = jwtProvider.getClaimsFromSignUpToken(actual);
		String actualEmail = (String)claims.get("email");
		assertThat(actualEmail).isEqualTo(email);
	}
}
