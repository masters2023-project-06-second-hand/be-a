package com.codesquad.secondhand.domain.region.dto.response;

import com.codesquad.secondhand.domain.region.entity.Region;

import lombok.Getter;

@Getter
public class RegionsResponse {
	private Long id;
	private String name;

	public RegionsResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static RegionsResponse from(Region region) {
		return new RegionsResponse(region.getId(), region.getName());
	}
}
