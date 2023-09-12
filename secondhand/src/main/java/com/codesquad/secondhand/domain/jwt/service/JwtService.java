package com.codesquad.secondhand.domain.jwt.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.jwt.domain.Jwt;
import com.codesquad.secondhand.domain.jwt.domain.JwtProvider;
import com.codesquad.secondhand.domain.jwt.entity.Token;
import com.codesquad.secondhand.domain.jwt.repository.TokenJpaRepository;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberQueryService;
import com.codesquad.secondhand.redis.util.RedisUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtService {
	private final RedisUtil redisUtil;
	private final TokenJpaRepository tokenJpaRepository;
	private final JwtProvider jwtProvider;
	private final MemberQueryService memberQueryService;

	@Transactional
	public void deleteRefreshToken(Long memberId) {
		tokenJpaRepository.deleteByMemberId(memberId);
	}

	@Transactional
	public void setBlackList(String accessToken) {
		redisUtil.setBlackList(accessToken, "accessToken", 60);
	}

	/**
	 * memberId 를 받아 accessToken 및 refreshToken 을 생성후
	 * memberId 와 refreshToken 을 db에 저장한다.
	 * @param memberId
	 * @return Jwt
	 */
	@Transactional
	public Jwt createTokens(Long memberId) {
		Jwt jwt = jwtProvider.createTokens(Map.of("memberId", memberId));
		Member member = memberQueryService.findById(memberId);
		Token token = Token.of(member, jwt);
		tokenJpaRepository.save(token);
		return jwt;
	}
}
