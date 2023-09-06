package com.codesquad.secondhand.domain.category.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.category.dto.response.CategoryResponse;
import com.codesquad.secondhand.domain.category.entity.Category;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

	private final CategoryQueryService CategoryQueryService;

	public List<CategoryResponse> findAll(Boolean includeImages) {
		List<Category> categories = CategoryQueryService.findAll();
		return categories.stream()
			.map(category -> CategoryResponse.of(category, includeImages))
			.collect(Collectors.toUnmodifiableList());
	}

}
