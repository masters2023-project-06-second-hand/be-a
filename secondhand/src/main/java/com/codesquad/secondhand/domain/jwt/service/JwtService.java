package com.codesquad.secondhand.domain.jwt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.token.repository.TokenJpaRepository;
import com.codesquad.secondhand.redis.util.RedisUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtService {
	private final RedisUtil redisUtil;
	private final TokenJpaRepository tokenJpaRepository;

	public void deleteRefreshToken(Long memberId) {
		tokenJpaRepository.deleteByMemberId(memberId);
	}

	public void setBlackList(String accessToken) {
		redisUtil.setBlackList(accessToken, "accessToken", 60);
	}
}
