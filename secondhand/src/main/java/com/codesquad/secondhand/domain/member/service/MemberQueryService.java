package com.codesquad.secondhand.domain.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.repository.MemberJpaRepository;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.MemberException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

	private final MemberJpaRepository memberJpaRepository;

	public Member findById(Long memberId) {
		return memberJpaRepository.findById(memberId).orElseThrow(() -> new CustomRuntimeException(
			MemberException.MEMBER_NOT_FOUND));
	}

	@Transactional
	public void save(Member member) {
		memberJpaRepository.save(member);
	}

	public Boolean existsByNickname(String nickname) {
		return memberJpaRepository.existsByNickname(nickname);
	}

	public Optional<Member> findByEmail(String email) {
		return memberJpaRepository.findByEmail(email);
	}

}
