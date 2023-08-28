package com.codesquad.secondhand.domain.category.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codesquad.secondhand.annotation.IntegrationTest;
import com.codesquad.secondhand.domain.category.service.CategoryService;

@IntegrationTest
class CategoryControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	CategoryService categoryService;

	@Test
	@DisplayName("카테고리 목록 조회를 통해 이미지를 포함한 모든 카테고리 목록을 조회할 수 있다.")
	void findAllCategoryIncludeImage() throws Exception {
		//when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/categories?includeImages=true")
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
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].id").exists())
			.andExpect(jsonPath("$[0].name").exists())
			.andExpect(jsonPath("$[0].imgUrl").doesNotExist())
			.andExpect(status().isOk());
	}

}
