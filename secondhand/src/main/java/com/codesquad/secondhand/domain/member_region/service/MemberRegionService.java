package com.codesquad.secondhand.domain.member_region.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member_region.entity.MemberRegion;
import com.codesquad.secondhand.domain.region.entity.Region;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.RegionException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRegionService {

	private final MemberRegionQueryService memberRegionQueryService;

	@Transactional
	public void save(MemberRegion memberRegion) {
		validate(memberRegion.getMember(), memberRegion.getRegion());
		memberRegionQueryService.save(memberRegion);
	}

	private void validate(Member member, Region region) {
		validateSize(member);
		validateDuplicate(member, region);
	}

	private void validateSize(Member member) {
		int count = memberRegionQueryService.countByMember(member);
		if (count == 2) {
			throw new CustomRuntimeException(RegionException.REGION_SIZE);
		}
	}

	private void validateDuplicate(Member member, Region region) {
		Boolean check = memberRegionQueryService.existsByMemberAndRegion(member, region);
		if (check) {
			throw new CustomRuntimeException(RegionException.REGION_DUPLICATED);
		}
	}
}
