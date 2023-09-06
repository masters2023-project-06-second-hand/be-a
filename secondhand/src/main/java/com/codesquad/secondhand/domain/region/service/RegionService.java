package com.codesquad.secondhand.domain.region.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.region.dto.response.RegionSearchAndPageResponse;
import com.codesquad.secondhand.domain.region.entity.Region;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {
	private final RegionQueryService regionQueryService;

	public List<RegionSearchAndPageResponse> getAllAndSearch(Pageable pageable, String word) {
		List<Region> regions = regionQueryService.findAll(pageable, word);
		List<RegionSearchAndPageResponse> response = regions.stream()
			.map(RegionSearchAndPageResponse::from)
			.collect(Collectors.toUnmodifiableList());
		return response;
	}
}
