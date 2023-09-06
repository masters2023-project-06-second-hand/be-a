package com.codesquad.secondhand.domain.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.product.repository.ProductJpaRepository;
import com.codesquad.secondhand.domain.product.repository.ProductQueryRepository;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.ProductException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService {

	private final ProductJpaRepository productJpaRepository;
	private final ProductQueryRepository productQueryRepository;

	@Transactional
	public Long save(Product product) {
		return productJpaRepository.save(product).getId();
	}

	@Transactional
	public void deleteById(Long productId) {
		productJpaRepository.deleteById(productId);
	}

	public List<Product> findSalesProduct(Member member, Integer statusId) {
		return productQueryRepository.findSalesProduct(member, statusId);
	}

	public Product findById(Long productId) {
		return productJpaRepository.findById(productId)
			.orElseThrow(() -> new CustomRuntimeException(ProductException.NOT_FOUND_PRODUCT));
	}

	public List<Product> findAll(Long regionId, Long categoryId) {
		return productQueryRepository.findAll(regionId, categoryId);
	}
}
