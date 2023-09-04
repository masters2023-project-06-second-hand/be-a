package com.codesquad.secondhand.domain.member.dto.response;

import com.codesquad.secondhand.domain.member_region.entity.MemberRegion;


import lombok.Getter;

@Getter
public class Regions {
	private Long id;
	private String name;

	public Regions(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static Regions from(MemberRegion memberRegion) {
		return new Regions(memberRegion.getRegion().getId(), memberRegion.getRegion().getName());
	}
}
