package com.codesquad.secondhand.domain.member.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class RegionResponse {

	private Long selectedRegionId;
	private List<Regions> regions;

}
