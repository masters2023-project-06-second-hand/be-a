package com.codesquad.secondhand.domain.member.dto.response;

import com.codesquad.secondhand.domain.member.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponse {

	private Long id;
	private String nickname;
	private String profileImg;

	public static MemberInfoResponse from(Member member) {
		return MemberInfoResponse.builder()
			.id(member.getId())
			.nickname(member.getNickname())
			.profileImg(member.getProfileImg())
			.build();
	}
}
