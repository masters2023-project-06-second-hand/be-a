package com.codesquad.secondhand.domain.category.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codesquad.secondhand.domain.category.dto.response.CategoryResponse;
import com.codesquad.secondhand.domain.category.entity.Category;
import com.codesquad.secondhand.domain.category.repository.CategoryJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryJpaRepository categoryJpaRepository;

	public List<CategoryResponse> findAll(Boolean includeImages) {
		List<Category> categories = categoryJpaRepository.findAll();
		return categories.stream()
			.map(category -> CategoryResponse.of(category, includeImages))
			.collect(Collectors.toUnmodifiableList());
	}
}
