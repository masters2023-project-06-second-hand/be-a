package com.codesquad.secondhand.domain.category.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.codesquad.secondhand.annotation.ServiceIntegrationTest;
import com.codesquad.secondhand.domain.category.dto.response.CategoryResponse;

@ServiceIntegrationTest
public class CategoryServiceTest {

	@Autowired
	CategoryService categoryService;

	@Test
	@DisplayName("includeImages 가 true일때, category 전체 목록조회시 이미지를 포함한 카테고리 목록이 리턴된다.")
	void findAllCategoryIncludeImage() {
		//given
		Boolean includeImages = true;

		//when
		List<CategoryResponse> categoryResponses = categoryService.findAll(includeImages);

		//then
		boolean allImageUrlsNotNull = categoryResponses.stream()
			.allMatch(categoryResponse -> categoryResponse.getImgUrl() != null);

		Assertions.assertThat(allImageUrlsNotNull).isTrue();
	}

	@Test
	@DisplayName("includeImages 가 false일때, category 전체 목록조회시 이미지를 포함하지않은 카테고리 목록이 리턴된다.")
	void findAllCategoryNotIncludingImage() {
		//given
		Boolean includeImages = false;

		//when
		List<CategoryResponse> categoryResponses = categoryService.findAll(includeImages);

		//then
		boolean allImageUrlsNotNull = categoryResponses.stream()
			.allMatch(categoryResponse -> categoryResponse.getImgUrl() == null);

		Assertions.assertThat(allImageUrlsNotNull).isTrue();
	}
}
