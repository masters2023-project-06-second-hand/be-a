package com.codesquad.secondhand.domain.member.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

/**
 * 특정 멤버가 선택한 지역 목록을 담은 response
 */
@Getter
@Builder
public class MemberRegionResponse {

	private Long selectedRegionId;
	private List<RegionResponse> regions;

	public static MemberRegionResponse of(Long selectedRegionId, List<RegionResponse> regions) {
		return MemberRegionResponse.builder()
			.selectedRegionId(selectedRegionId)
			.regions(regions)
			.build();
	}

}
