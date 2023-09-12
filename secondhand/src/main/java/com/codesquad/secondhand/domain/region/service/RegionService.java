package com.codesquad.secondhand.domain.region.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.region.dto.response.RegionSearchAndPageResponse;
import com.codesquad.secondhand.domain.region.dto.response.RegionsResponse;
import com.codesquad.secondhand.domain.region.entity.Region;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {
	private final RegionQueryService regionQueryService;

	public RegionSearchAndPageResponse getAllAndSearch(Pageable pageable, String word) {
		Slice<Region> regions = regionQueryService.findAll(pageable, word);

		List<RegionsResponse> regionsResponse = regions.getContent().stream()
			.map(RegionsResponse::from)
			.collect(Collectors.toUnmodifiableList());

		RegionSearchAndPageResponse response = RegionSearchAndPageResponse.builder()
			.hasNext(regions.hasNext())
			.page(regions.getNumber())
			.regionsResponses(regionsResponse)
			.build();
		return response;
	}
}
