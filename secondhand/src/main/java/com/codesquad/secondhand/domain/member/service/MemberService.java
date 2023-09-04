package com.codesquad.secondhand.domain.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.jwt.service.JwtService;
import com.codesquad.secondhand.domain.member.dto.request.RegionRequest;
import com.codesquad.secondhand.domain.member.dto.request.SignupRequest;
import com.codesquad.secondhand.domain.member.dto.response.RegionResponse;
import com.codesquad.secondhand.domain.member.dto.response.Regions;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.repository.MemberJpaRepository;
import com.codesquad.secondhand.domain.member_region.entity.MemberRegion;
import com.codesquad.secondhand.domain.member_region.service.MemberRegionService;
import com.codesquad.secondhand.domain.region.entity.Region;
import com.codesquad.secondhand.domain.region.service.RegionService;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.MemberException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
	private final MemberJpaRepository memberJpaRepository;
	private final RegionService regionService;
	private final MemberRegionService memberRegionService;
	private final JwtService jwtService;

	public Member findById(Long memberId) {
		return memberJpaRepository.findById(memberId).orElseThrow(() -> new CustomRuntimeException(
			MemberException.MEMBER_NOT_FOUND));
	}

	@Transactional
	public void signUp(SignupRequest signupRequest, String email) {
		validDuplicatedName(signupRequest.getNickname());
		Member member = signupRequest.toEntity(email);

		memberJpaRepository.save(member);
		List<Region> regions = regionService.findByIds(signupRequest.getRegionsId());
		List<MemberRegion> memberRegions = MemberRegion.of(member, regions);
		memberRegionService.saveAll(memberRegions);
	}

	private void validDuplicatedName(String nickname) {
		if (memberJpaRepository.existsByNickname(nickname)) {
			throw new CustomRuntimeException(MemberException.MEMBER_NICKNAME_EXIST);
		}
	}

	public void signOut(String accessToken, Long memberId) {
		jwtService.deleteRefreshToken(memberId);
		jwtService.setBlackList(accessToken);
	}

	@Transactional
	public void addRegion(Long memberId, RegionRequest regionRequest) {
		Member member = findById(memberId);
		Region region = regionService.findById(regionRequest.getId());
		MemberRegion memberRegion = MemberRegion.builder()
			.member(member)
			.region(region)
			.build();
		memberRegionService.save(memberRegion);

	}

	@Transactional
	public void deleteRegion(Long memberId, RegionRequest regionRequest) {
		Member member = findById(memberId);
		Region region = regionService.findById(regionRequest.getId());
		MemberRegion memberRegion = memberRegionService.findByMemberAndRegion(member,region);
		memberRegionService.delete(memberRegion);
	}

	@Transactional
	public void setSelectedRegion(Long memberId, RegionRequest regionRequest) {
		Member member = findById(memberId);
		Region region = regionService.findById(regionRequest.getId());
		memberRegionService.findByMemberAndRegion(member,region);
		member.addSelectedRegion(region.getId());
	}

	public RegionResponse getRegion(Long memberId) {
		Member member = findById(memberId);
		Long selectedRegionId = member.getSelectedRegion();
		List<MemberRegion> memberRegions = memberRegionService.findAllMemberRegion(memberId);
		List<Regions> regions = memberRegions.stream()
			.map(Regions::from)
			.collect(Collectors.toList());
		return RegionResponse.builder()
			.selectedRegionId(selectedRegionId)
			.regions(regions)
			.build();
	}
}
