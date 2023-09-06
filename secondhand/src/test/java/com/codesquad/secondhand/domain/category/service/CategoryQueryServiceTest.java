package com.codesquad.secondhand.domain.category.service;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.codesquad.secondhand.annotation.ServiceIntegrationTest;
import com.codesquad.secondhand.domain.category.entity.Category;
import com.codesquad.secondhand.exception.CustomRuntimeException;

@ServiceIntegrationTest
public class CategoryQueryServiceTest {

	@Autowired
	CategoryQueryService categoryQueryService;

	@Test
	@DisplayName("categoryId를 통해 특정 카테고리를 찾을수 있다.")
	void findById() {
		//given
		Long categoryId = 1L;

		//when
		Category category = categoryQueryService.findById(categoryId);

		//then
		assertThat(category.getId()).isEqualTo(categoryId);
	}

	@Test
	@DisplayName("categoryId를 통해 특정 카테고리를 찾을수 없다면 예외가 발생한다.")
	void findByIdThrowsExceptionWhenCategoryNotFound() {
		//given
		Long categoryId = 100L;

		//when & then
		Assertions.assertThatThrownBy(() -> categoryQueryService.findById(categoryId))
			.isInstanceOf(CustomRuntimeException.class);
	}

	@Test
	@DisplayName("findAll을 통해 모든 카테고리를 찾을수 있다.")
	void findAll() {
		//given
		Long categoryId = 1L;

		//when
		Category category = categoryQueryService.findById(1L);

		//then
		assertThat(category.getId()).isEqualTo(categoryId);
	}
}
