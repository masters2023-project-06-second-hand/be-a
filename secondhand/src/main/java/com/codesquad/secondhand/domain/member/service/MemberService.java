package com.codesquad.secondhand.domain.member.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.jwt.service.JwtService;
import com.codesquad.secondhand.domain.member.dto.request.SignupRequest;
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
}
