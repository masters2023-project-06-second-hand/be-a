package com.codesquad.secondhand.domain.member_region.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.member.dto.request.RegionRequest;
import com.codesquad.secondhand.domain.member.dto.response.MemberRegionResponse;
import com.codesquad.secondhand.domain.member.dto.response.RegionResponse;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberQueryService;
import com.codesquad.secondhand.domain.member_region.entity.MemberRegion;
import com.codesquad.secondhand.domain.region.entity.Region;
import com.codesquad.secondhand.domain.region.service.RegionQueryService;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.MemberException;
import com.codesquad.secondhand.exception.errorcode.RegionException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRegionService {

	public static final int MIN_REGION_COUNT = 1;
	public static final int MAX_REGION_COUNT = 2;
	private final MemberRegionQueryService memberRegionQueryService;
	private final MemberQueryService memberQueryService;
	private final RegionQueryService regionQueryService;

	@Transactional
	public void addRegion(Long memberId, RegionRequest regionRequest) {
		Member member = memberQueryService.findById(memberId);
		Region region = regionQueryService.findById(regionRequest.getId());
		MemberRegion memberRegion = MemberRegion.of(member, region);
		validate(memberRegion.getMember(), memberRegion.getRegion());
		memberRegionQueryService.save(memberRegion);
	}

	@Transactional
	public void deleteRegion(Long memberId, RegionRequest regionRequest) {
		List<MemberRegion> memberRegions = memberRegionQueryService.findAllMemberRegion(memberId);
		if (memberRegions.size() == MIN_REGION_COUNT) {
			throw new CustomRuntimeException(MemberException.MEMBER_REGION_DELETE_FAIlED);
		}
		Member member = memberQueryService.findById(memberId);
		Region region = regionQueryService.findById(regionRequest.getId());
		MemberRegion memberRegion = memberRegionQueryService.findByMemberAndRegion(member, region);
		memberRegionQueryService.delete(memberRegion);
		for (MemberRegion remainingRegion : memberRegions) {
			if (!remainingRegion.getRegion().getId().equals(regionRequest.getId())) {
				member.addSelectedRegion(remainingRegion.getRegion().getId());
			}
		}
	}

	@Transactional
	public void updateSelectedRegion(Long memberId, RegionRequest regionRequest) {
		Member member = memberQueryService.findById(memberId);
		Region region = regionQueryService.findById(regionRequest.getId());
		memberRegionQueryService.findByMemberAndRegion(member, region);
		member.addSelectedRegion(region.getId());
	}

	public MemberRegionResponse getRegion(Long memberId) {
		Member member = memberQueryService.findById(memberId);
		Long selectedRegionId = member.getSelectedRegion();
		List<MemberRegion> memberRegions = memberRegionQueryService.findAllMemberRegion(memberId);
		List<RegionResponse> regions = memberRegions.stream()
			.map(RegionResponse::from)
			.collect(Collectors.toList());
		return MemberRegionResponse.of(selectedRegionId, regions);
	}

	private void validate(Member member, Region region) {
		validateSize(member);
		validateDuplicate(member, region);
	}

	/**
	 * 사용자 지역 설정 최대 갯수가 2개이기 때문에 이미 설정된 지역이 2개이상 이면 예외를 발생시킨다.
	 *
	 * @param member
	 */
	private void validateSize(Member member) {
		int count = memberRegionQueryService.countByMember(member);
		if (count == MAX_REGION_COUNT) {
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
