package com.codesquad.secondhand.domain.jwt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.jwt.entity.Token;
import com.codesquad.secondhand.domain.jwt.repository.TokenJpaRepository;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.JwtException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtQueryService {

	private final TokenJpaRepository tokenJpaRepository;

	@Transactional
	public void delete(Long memberId) {
		tokenJpaRepository.deleteByMemberId(memberId);
	}

	@Transactional
	public void save(Token token) {
		tokenJpaRepository.save(token);
	}

	public Token findByRefreshToken(String refreshToken) {
		return tokenJpaRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new CustomRuntimeException(JwtException.REFRESH_TOKEN_NOT_FOUND_EXCEPTION));
	}
}
