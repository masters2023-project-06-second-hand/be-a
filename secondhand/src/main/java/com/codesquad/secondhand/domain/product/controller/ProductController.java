package com.codesquad.secondhand.domain.product.controller;

import static com.codesquad.secondhand.common.util.RequestParser.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.domain.product.dto.request.ProductSaveAndUpdateRequest;
import com.codesquad.secondhand.domain.product.dto.request.ProductUpdateRequest;
import com.codesquad.secondhand.domain.product.dto.response.ProductDetailResponse;
import com.codesquad.secondhand.domain.product.dto.response.ProductFindAllResponse;
import com.codesquad.secondhand.domain.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {

	private final ProductService productService;

	@PostMapping("/products")
	public ResponseEntity<Map<String, Long>> save(
		@Valid @RequestBody ProductSaveAndUpdateRequest productSaveAndUpdateRequest,
		HttpServletRequest request) {
		Long memberId = extractMemberId(request);
		Long productId = productService.save(productSaveAndUpdateRequest, memberId);
		return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("id", productId));
	}

	@GetMapping("/products/{productId}")
	public ResponseEntity<ProductDetailResponse> findDetail(@PathVariable Long productId) {
		ProductDetailResponse productDetailResponse = productService.findDetail(productId);
		return ResponseEntity.ok().body(productDetailResponse);
	}

	@PutMapping("/products/{productId}")
	public ResponseEntity update(@PathVariable Long productId,
		@Valid @RequestBody ProductSaveAndUpdateRequest request) {
		productService.update(productId, request);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/products/{productId}")
	public ResponseEntity delete(@PathVariable Long productId) {
		productService.delete(productId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/products/{productId}/status")
	public ResponseEntity updateStatus(@PathVariable Long productId,
		@RequestBody ProductUpdateRequest productUpdateRequest) {
		productService.updateStatus(productId, productUpdateRequest);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/products")
	public ResponseEntity<List<ProductFindAllResponse>> findAll(@RequestParam Long regionId,
		@RequestParam(required = false) Long categoryId) {
		return ResponseEntity.ok().body(productService.findAll(regionId, categoryId));
	}

	@GetMapping("/members/{memberId}/sales")
	public ResponseEntity<List<ProductFindAllResponse>> findSalesProducts(@PathVariable Long memberId,
		@RequestParam(required = false) Integer statusId) {
		return ResponseEntity.ok().body(productService.findSalesProducts(memberId, statusId));
	}
}
