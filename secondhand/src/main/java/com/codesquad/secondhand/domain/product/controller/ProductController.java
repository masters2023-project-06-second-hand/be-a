package com.codesquad.secondhand.domain.product.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.domain.product.dto.request.ProductSaveRequestDto;
import com.codesquad.secondhand.domain.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {

	private final ProductService productService;

	@PostMapping("/products")
	public ResponseEntity<Map<String, Long>> save(@RequestBody ProductSaveRequestDto productSaveRequestDto) {
		Long productId = productService.save(productSaveRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("id", productId));
	}
}
