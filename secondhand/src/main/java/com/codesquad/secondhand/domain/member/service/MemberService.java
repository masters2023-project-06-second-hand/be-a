package com.codesquad.secondhand.domain.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.jwt.service.JwtService;
import com.codesquad.secondhand.domain.member.dto.request.RegionRequest;
import com.codesquad.secondhand.domain.member.dto.request.SignupRequest;
import com.codesquad.secondhand.domain.member.dto.response.MemberRegionResponse;
import com.codesquad.secondhand.domain.member.dto.response.RegionResponse;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member_region.entity.MemberRegion;
import com.codesquad.secondhand.domain.member_region.service.MemberRegionQueryService;
import com.codesquad.secondhand.domain.region.entity.Region;
import com.codesquad.secondhand.domain.region.service.RegionQueryService;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.MemberException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
	private final MemberQueryService memberQueryService;
	private final RegionQueryService regionQueryService;
	private final MemberRegionQueryService memberRegionQueryService;
	private final JwtService jwtService;

	@Transactional
	public void signUp(SignupRequest signupRequest, String email) {
		validDuplicatedName(signupRequest.getNickname());
		Member member = signupRequest.toEntity(email);

		memberQueryService.save(member);
		List<Region> regions = regionQueryService.findByIds(signupRequest.getRegionsId());
		List<MemberRegion> memberRegions = MemberRegion.of(member, regions);
		memberRegionQueryService.saveAll(memberRegions);
	}

	private void validDuplicatedName(String nickname) {
		if (memberQueryService.existsByNickname(nickname)) {
			throw new CustomRuntimeException(MemberException.MEMBER_NICKNAME_EXIST);
		}
	}

	@Transactional
	public void signOut(String accessToken, Long memberId) {
		jwtService.deleteRefreshToken(memberId);
		jwtService.setBlackList(accessToken);
	}

	@Transactional
	public void addRegion(Long memberId, RegionRequest regionRequest) {
		Member member = memberQueryService.findById(memberId);
		Region region = regionQueryService.findById(regionRequest.getId());
		MemberRegion memberRegion = MemberRegion.of(member, region);
		memberRegionQueryService.save(memberRegion);

	}

	@Transactional
	public void deleteRegion(Long memberId, RegionRequest regionRequest) {
		Member member = memberQueryService.findById(memberId);
		Region region = regionQueryService.findById(regionRequest.getId());
		MemberRegion memberRegion = memberRegionQueryService.findByMemberAndRegion(member, region);
		memberRegionQueryService.delete(memberRegion);
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
}
