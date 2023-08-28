package com.codesquad.secondhand.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.repository.MemberJpaRepository;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.MemberException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
	private final MemberJpaRepository memberJpaRepository;

	public Member findById(Long memberId) {
		return memberJpaRepository.findById(memberId).orElseThrow(() -> new CustomRuntimeException(
			MemberException.MEMBER_NOT_FOUND));
	}
}
