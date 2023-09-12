package com.codesquad.secondhand.domain.region.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.codesquad.secondhand.annotation.ServiceIntegrationTest;
import com.codesquad.secondhand.domain.region.dto.response.RegionSearchAndPageResponse;

@ServiceIntegrationTest
class RegionServiceTest {

	@Autowired
	RegionService regionService;

	@DisplayName("페이징 및 검색 (검색어가 없으면 페이징만 동작) ")
	@Test
	void getAllAndSearch() {
		// given
		int requestPage = 5;
		int requestOffset = 0;
		Pageable pageable = PageRequest.of(requestOffset, requestPage);
		// when
		RegionSearchAndPageResponse actual = regionService.getAllAndSearch(pageable, null);
		// then
		assertThat(actual.getRegionsResponses().size()).isEqualTo(requestPage);

	}
}
