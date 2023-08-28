package com.codesquad.secondhand.domain.category.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.category.dto.response.CategoryResponse;
import com.codesquad.secondhand.domain.category.entity.Category;
import com.codesquad.secondhand.domain.category.repository.CategoryJpaRepository;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.CategoryException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

	private final CategoryJpaRepository categoryJpaRepository;

	public List<CategoryResponse> findAll(Boolean includeImages) {
		List<Category> categories = categoryJpaRepository.findAll();
		return categories.stream()
			.map(category -> CategoryResponse.of(category, includeImages))
			.collect(Collectors.toUnmodifiableList());
	}

	public Category findById(Long categoryId) {
		return categoryJpaRepository.findById(categoryId).orElseThrow(() -> new CustomRuntimeException(
			CategoryException.CATEGORY_NOT_FOUND));
	}
}
