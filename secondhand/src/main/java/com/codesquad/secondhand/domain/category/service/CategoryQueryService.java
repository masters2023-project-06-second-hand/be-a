package com.codesquad.secondhand.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.category.entity.Category;
import com.codesquad.secondhand.domain.category.repository.CategoryJpaRepository;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.CategoryException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryService {

	private final CategoryJpaRepository categoryJpaRepository;

	public Category findById(Long categoryId) {
		return categoryJpaRepository.findById(categoryId).orElseThrow(() -> new CustomRuntimeException(
			CategoryException.CATEGORY_NOT_FOUND));
	}

	public List<Category> findAll() {
		return categoryJpaRepository.findAll();
	}
}
