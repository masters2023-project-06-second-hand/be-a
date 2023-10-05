package com.codesquad.secondhand.domain.jwt.service;

import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.jwt.domain.Jwt;
import com.codesquad.secondhand.domain.jwt.domain.JwtProvider;
import com.codesquad.secondhand.domain.jwt.dto.request.ReissueTokenRequest;
import com.codesquad.secondhand.domain.jwt.dto.response.ReissueTokenResponse;
import com.codesquad.secondhand.domain.jwt.entity.Token;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberQueryService;
import com.codesquad.secondhand.redis.util.RedisUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtService {
	private final RedisUtil redisUtil;
	private final JwtProvider jwtProvider;
	private final JwtQueryService jwtQueryService;
	private final MemberQueryService memberQueryService;

	@Transactional
	public void deleteRefreshToken(Long memberId) {
		jwtQueryService.delete(memberId);
	}

	@Transactional
	public void setBlackList(String accessToken) {
		redisUtil.setBlackList(accessToken, "accessToken", 60);
	}

	/**
	 * memberId 를 받아 accessToken 및 refreshToken 을 생성후
	 * memberId 와 refreshToken 을 db에 저장한다.
	 * createTokens 메서드는 로그인, 회원가입때 모두 사용된다. 따라서 isSignIn을 통해 분기처리를 해줬다.
	 *
	 * @param memberId
	 * @return Jwt
	 */
	@Transactional
	public Jwt createTokens(Long memberId, Boolean isSignIn) {
		Jwt jwt = jwtProvider.createTokens(Collections.singletonMap("memberId", memberId));
		Member member = memberQueryService.findById(memberId);
		Token token = Token.of(member, jwt);
		if (isSignIn) {
			jwtQueryService.delete(memberId);
		}
		jwtQueryService.save(token);
		return jwt;
	}

	public Jwt createSignUpToken(String email) {
		return jwtProvider.createSignUpToken(Collections.singletonMap("email", email));
	}

	public ReissueTokenResponse reissueToken(ReissueTokenRequest reissueTokenRequest) {
		Token token = jwtQueryService.findByRefreshToken(reissueTokenRequest.getRefreshToken());
		String reissuedAccessToken = jwtProvider.reissueAccessToken(
			Collections.singletonMap("memberId", token.getMember().getId()));
		return ReissueTokenResponse.of(token, reissuedAccessToken);
	}

}
