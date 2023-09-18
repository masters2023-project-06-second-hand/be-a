package com.codesquad.secondhand.domain.region.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codesquad.secondhand.BaseControllerTest;
import com.codesquad.secondhand.annotation.ControllerIntegrationTest;

@ControllerIntegrationTest
class RegionControllerTest extends BaseControllerTest {

	@Test
	@DisplayName("검색이 없을 때 지역을 5개씩 보여준다")
	void getPagingRegions() throws Exception {
		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/regions")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.regions", hasSize(5)))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("검색 할 word 가 있으면 word 가 포함된 지역을 보여준다.")
	void getSearchRegions() throws Exception {
		// when & then
		String word = "관악";
		mockMvc.perform(MockMvcRequestBuilders.get("/api/regions/")
				.param("word", word)
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.regions", hasSize(1)))
			.andExpect(jsonPath("$.regions[0].name", is("서울 관악동"))) // 검색 결과에서 기대하는 객체의 name 값을 확인
			.andExpect(status().isOk());
	}

}
