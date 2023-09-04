package com.codesquad.secondhand.domain.region.dto.response;

import com.codesquad.secondhand.domain.region.entity.Region;

import lombok.Getter;

@Getter
public class RegionSearchAndPageResponse {
	private Long id;
	private String name;

	public RegionSearchAndPageResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static RegionSearchAndPageResponse from(Region region) {
		return new RegionSearchAndPageResponse(region.getId(), region.getName());
	}
}
