package com.codesquad.secondhand.domain.category.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codesquad.secondhand.BaseControllerTest;
import com.codesquad.secondhand.annotation.ControllerIntegrationTest;

@ControllerIntegrationTest
class CategoryControllerTest extends BaseControllerTest {

	@Test
	@DisplayName("카테고리 목록 조회를 통해 이미지를 포함한 모든 카테고리 목록을 조회할 수 있다.")
	void findAllCategoryIncludeImage() throws Exception {
		//when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/categories?includeImages=true")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].id").exists())
			.andExpect(jsonPath("$[0].name").exists())
			.andExpect(jsonPath("$[0].imgUrl").exists())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("카테고리 목록 조회를 통해 이미지를 포함하지 않은 모든 카테고리 목록을 조회할 수 있다.")
	void findAllCategoryExcludeImage() throws Exception {
		//when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/categories?includeImages=false")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].id").exists())
			.andExpect(jsonPath("$[0].name").exists())
			.andExpect(jsonPath("$[0].imgUrl").doesNotExist())
			.andExpect(status().isOk());
	}
}
