package com.codesquad.secondhand.domain.member_region.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.member_region.entity.MemberRegion;
import com.codesquad.secondhand.domain.member_region.repository.MemberRegionJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRegionService {

	private final MemberRegionJpaRepository memberRegionJpaRepository;

	public void saveAll(List<MemberRegion> memberRegion) {
		memberRegionJpaRepository.saveAll(memberRegion);
	}
}
