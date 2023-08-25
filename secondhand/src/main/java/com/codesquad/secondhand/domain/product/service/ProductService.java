package com.codesquad.secondhand.domain.product.service;

import org.springframework.stereotype.Service;

import com.codesquad.secondhand.domain.category.entity.Category;
import com.codesquad.secondhand.domain.category.repository.CategoryJpaRepository;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.repository.MemberJpaRepository;
import com.codesquad.secondhand.domain.product.dto.request.ProductSaveRequestDto;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.product.repository.ProductJpaRepository;
import com.codesquad.secondhand.domain.region.entity.Region;
import com.codesquad.secondhand.domain.region.repository.RegionJpaRepository;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.CategoryException;
import com.codesquad.secondhand.exception.errorcode.MemberException;
import com.codesquad.secondhand.exception.errorcode.RegionException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {

	public static final long DUMMY_MEMBER_ID = 1L;
	private final ProductJpaRepository productJpaRepository;
	private final CategoryJpaRepository categoryJpaRepository;
	private final RegionJpaRepository regionJpaRepository;
	private final MemberJpaRepository memberJpaRepository;

	public Long save(ProductSaveRequestDto productSaveRequestDto) {
		Category category = categoryJpaRepository.findById(productSaveRequestDto.getCategoryId())
			.orElseThrow(() -> new CustomRuntimeException(
				CategoryException.CATEGORY_NOT_FOUND));
		Region region = regionJpaRepository.findById(productSaveRequestDto.getRegionId())
			.orElseThrow(() -> new CustomRuntimeException(
				RegionException.REGION_NOT_FOUND));
		Member member = memberJpaRepository.findById(DUMMY_MEMBER_ID).orElseThrow(() -> new CustomRuntimeException(
			MemberException.MEMBER_NOT_FOUND));
		Product product = productSaveRequestDto.toEntity(category, region, member);
		return productJpaRepository.save(product).getId();
	}

}
