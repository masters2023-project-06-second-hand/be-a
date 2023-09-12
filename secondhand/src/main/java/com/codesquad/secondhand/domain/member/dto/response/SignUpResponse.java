package com.codesquad.secondhand.domain.member.dto.response;

import com.codesquad.secondhand.domain.jwt.domain.Jwt;
import com.codesquad.secondhand.domain.member.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpResponse {

	private Long memberId;
	private String accessToken;
	private String refreshToken;

	public static SignUpResponse of(Member member, Jwt jwt) {
		return SignUpResponse.builder()
			.memberId(member.getId())
			.accessToken(jwt.getAccessToken())
			.refreshToken(jwt.getRefreshToken())
			.build();
	}
}
