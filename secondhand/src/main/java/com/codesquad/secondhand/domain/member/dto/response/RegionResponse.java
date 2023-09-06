package com.codesquad.secondhand.domain.member.dto.response;

import com.codesquad.secondhand.domain.member_region.entity.MemberRegion;

import lombok.Getter;

@Getter
public class RegionResponse {
	private Long id;
	private String name;

	public RegionResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static RegionResponse from(MemberRegion memberRegion) {
		return new RegionResponse(memberRegion.getRegion().getId(), memberRegion.getRegion().getName());
	}
}
