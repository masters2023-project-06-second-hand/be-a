package com.codesquad.secondhand.domain.region.service;



import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.codesquad.secondhand.annotation.ServiceIntegrationTest;
import com.codesquad.secondhand.domain.region.entity.Region;
import com.codesquad.secondhand.exception.CustomRuntimeException;

@ServiceIntegrationTest
class RegionQueryServiceTest {

	@Autowired
	RegionQueryService regionQueryService;

	@DisplayName(" id로 지역을 찾을 수 있다.")
	@Test
	void findByIdSuccess() {
		// given
		Long regionId = 1L;
		// when
		Region actual = regionQueryService.findById(regionId);
		// then
		assertThat(actual.getId()).isEqualTo(regionId);

	}

	@DisplayName("존재하지 않는 지역을 입력하면 예외가 발생한다.")
	@Test
	void findByIdFailed() {
		// given
		Long regionId = 155L;
		// when&then
		assertThatThrownBy(() -> regionQueryService.findById(regionId)).isInstanceOf(CustomRuntimeException.class);
	}

	@DisplayName("페이지 시작(offset) 한 페이지당 보여줄 갯수(page) 로 5개씩 페이징")
	@Test
	void Paging() {
		// given
		int requestPage = 5;
		int requestOffset = 0;
		Pageable pageable = PageRequest.of(requestOffset,requestPage);
		// when
		List<Region> actual = regionQueryService.findAll(pageable,null);
		// then
		assertThat(actual.size()).isEqualTo(requestPage);
	}

	@DisplayName("지역 이름으로 검색")
	@Test
	void search() {
		// given
		Region region = regionQueryService.findById(1L);
		int requestPage = 5;
		int requestOffset = 0;
		String requestString = region.getName();
		Pageable pageable = PageRequest.of(requestOffset,requestPage);
		// when
		List<Region> actual = regionQueryService.findAll(pageable,requestString);
		// then
		assertThat(actual.size()).isEqualTo(1);
		assertThat(actual.get(0).getName()).isEqualTo(requestString);
	}

	@DisplayName("아이디 리스트를 받아 Region에 매핑시킨다.")
	@Test
	void findByIds() {
		// given
		List<Long> request = List.of(1L,2L);
		// when
		List<Region>actual = regionQueryService.findByIds(request);
		// then
		assertThat(actual.size()).isEqualTo(2);
		assertThat(actual.get(0).getId()).isEqualTo(request.get(0));
		assertThat(actual.get(1).getId()).isEqualTo(request.get(1));
	}

}
