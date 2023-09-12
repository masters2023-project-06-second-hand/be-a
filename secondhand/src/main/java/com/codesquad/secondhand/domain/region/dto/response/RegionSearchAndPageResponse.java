package com.codesquad.secondhand.domain.region.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegionSearchAndPageResponse {
	private Boolean hasNext;
	private int page;
	private List<RegionsResponse> regionsResponses;

}
