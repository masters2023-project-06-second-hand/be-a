package com.codesquad.secondhand.domain.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.domain.jwt.entity.Token;

public interface TokenJpaRepository extends JpaRepository<Token, Long> {

	void deleteByMemberId(Long memberId);
}
