package com.codesquad.secondhand.domain.member_region.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member_region.entity.MemberRegion;
import com.codesquad.secondhand.domain.member_region.repository.MemberRegionJpaRepository;
import com.codesquad.secondhand.domain.region.entity.Region;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.RegionException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRegionQueryService {

	private final MemberRegionJpaRepository memberRegionJpaRepository;

	@Transactional
	public void saveAll(List<MemberRegion> memberRegion) {
		memberRegionJpaRepository.saveAll(memberRegion);
	}

	@Transactional
	public void save(MemberRegion memberRegion) {
		memberRegionJpaRepository.save(memberRegion);
	}

	public int countByMember(Member member) {
		return memberRegionJpaRepository.countByMember(member);
	}

	public Boolean existsByMemberAndRegion(Member member, Region region) {
		return memberRegionJpaRepository.existsByMemberAndRegion(member, region);
	}

	public List<MemberRegion> findAllMemberRegion(Long memberId) {
		return memberRegionJpaRepository.findAllByMemberId(memberId);
	}

	public MemberRegion findByMemberAndRegion(Member member, Region region) {
		return memberRegionJpaRepository.findByMemberAndRegion(member, region)
			.orElseThrow(() -> new CustomRuntimeException(RegionException.MEMBER_REGION_NOT_ADDED));
	}

	@Transactional
	public void delete(MemberRegion memberRegion) {
		memberRegionJpaRepository.delete(memberRegion);
	}
}
