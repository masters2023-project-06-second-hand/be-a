package com.codesquad.secondhand.domain.member.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.jwt.domain.Jwt;
import com.codesquad.secondhand.domain.jwt.service.JwtService;
import com.codesquad.secondhand.domain.member.dto.request.SignupRequest;
import com.codesquad.secondhand.domain.member.dto.response.MemberInfoResponse;
import com.codesquad.secondhand.domain.member.dto.response.SignUpResponse;
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
	public static final boolean IS_SIGN_IN = false;
	private final MemberQueryService memberQueryService;
	private final RegionQueryService regionQueryService;
	private final MemberRegionQueryService memberRegionQueryService;
	private final JwtService jwtService;

	@Transactional
	public SignUpResponse signUp(SignupRequest signupRequest, String email) {
		validDuplicatedName(signupRequest.getNickname());
		Member member = signupRequest.toEntity(email);

		memberQueryService.save(member);
		List<Region> regions = regionQueryService.findByIds(signupRequest.getRegionsId());
		List<MemberRegion> memberRegions = MemberRegion.of(member, regions);
		memberRegionQueryService.saveAll(memberRegions);

		Jwt jwt = jwtService.createTokens(member.getId(), IS_SIGN_IN);
		return SignUpResponse.of(member, jwt);
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

	public MemberInfoResponse getMemberInfo(Long memberId) {
		Member member = memberQueryService.findById(memberId);
		return MemberInfoResponse.from(member);
	}
}
