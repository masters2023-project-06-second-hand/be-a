package com.codesquad.secondhand.domain.category.dto.response;

import com.codesquad.secondhand.domain.category.entity.Category;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponse {

	private Long id;
	private String name;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String imgUrl;

	public static CategoryResponse of(Category category, Boolean includeImages) {
		CategoryResponseBuilder builder = CategoryResponse.builder()
			.id(category.getId())
			.name(category.getName());

		if (includeImages) {
			builder.imgUrl(category.getImgUrl());
		}

		return builder.build();
	}
}
