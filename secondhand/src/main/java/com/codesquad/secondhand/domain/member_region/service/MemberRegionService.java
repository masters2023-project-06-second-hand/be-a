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
public class MemberRegionService {

	private final MemberRegionJpaRepository memberRegionJpaRepository;

	public void saveAll(List<MemberRegion> memberRegion) {
		memberRegionJpaRepository.saveAll(memberRegion);
	}

	@Transactional
	public void save(MemberRegion memberRegion) {
		validate(memberRegion.getMember(),memberRegion.getRegion());
		memberRegionJpaRepository.save(memberRegion);
	}

	private void validate(Member member, Region region) {
		validateSize(member);
		validateDuplicate(member,region);
	}

	private void validateSize(Member member) {
		int count = memberRegionJpaRepository.countByMember(member);
		if(count==2){
			throw new CustomRuntimeException(RegionException.REGION_SIZE);
		}
	}

	private void validateDuplicate(Member member,Region region ) {
		Boolean check = memberRegionJpaRepository.existsByMemberAndRegion(member,region);
		if(check) {
			throw new CustomRuntimeException(RegionException.REGION_DUPLICATED);
		}
	}
}
