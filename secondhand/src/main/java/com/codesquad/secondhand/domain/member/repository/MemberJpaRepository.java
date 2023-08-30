package com.codesquad.secondhand.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.domain.member.entity.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	Boolean existsByNickname(String nickname);
}
