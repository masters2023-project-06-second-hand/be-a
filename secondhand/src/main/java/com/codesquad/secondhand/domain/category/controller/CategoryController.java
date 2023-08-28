package com.codesquad.secondhand.domain.category.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.domain.category.dto.response.CategoryResponse;
import com.codesquad.secondhand.domain.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping(value = "/categories")
	public ResponseEntity<List<CategoryResponse>> findAll(@RequestParam Boolean includeImages) {
		List<CategoryResponse> categories = categoryService.findAll(includeImages);
		return ResponseEntity.ok().body(categories);
	}
}
