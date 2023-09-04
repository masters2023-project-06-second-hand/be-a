package com.codesquad.secondhand.domain.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.category.entity.Category;
import com.codesquad.secondhand.domain.category.service.CategoryService;
import com.codesquad.secondhand.domain.image.service.ImageService;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberService;
import com.codesquad.secondhand.domain.product.dto.request.ProductSaveAndUpdateRequest;
import com.codesquad.secondhand.domain.product.dto.request.ProductUpdateRequest;
import com.codesquad.secondhand.domain.product.dto.response.ProductDetailResponse;
import com.codesquad.secondhand.domain.product.entity.Image;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.product.repository.ProductJpaRepository;
import com.codesquad.secondhand.domain.product.repository.ProductQueryRepository;
import com.codesquad.secondhand.domain.product.utils.ProductStatus;
import com.codesquad.secondhand.domain.region.entity.Region;
import com.codesquad.secondhand.domain.region.service.RegionService;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.ProductException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductService {

	private final ProductJpaRepository productJpaRepository;
	private final ProductQueryRepository productQueryRepository;
	private final CategoryService categoryService;
	private final RegionService regionService;
	private final MemberService memberService;
	private final ImageService imageService;

	@Transactional
	public Long save(ProductSaveAndUpdateRequest productSaveAndUpdateRequest, Long memberId) {
		Category category = categoryService.findById(productSaveAndUpdateRequest.getCategoryId());
		Region region = regionService.findById(productSaveAndUpdateRequest.getRegionId());
		Member member = memberService.findById(memberId);
		Image thumbnailImage = imageService.findById(productSaveAndUpdateRequest.getImagesId().get(0));
		Product product = productSaveAndUpdateRequest.toEntity(category, region, member, thumbnailImage);
		imageService.updateProductId(productSaveAndUpdateRequest.getImagesId(), product);

		return productJpaRepository.save(product).getId();
	}

	public ProductDetailResponse findDetail(Long productId) {
		return ProductDetailResponse.from(findById(productId));
	}

	@Transactional
	public void update(Long productId, ProductSaveAndUpdateRequest productSaveAndUpdateRequest) {
		Product product = findById(productId);
		Category category = categoryService.findById(productSaveAndUpdateRequest.getCategoryId());
		Region region = regionService.findById(productSaveAndUpdateRequest.getRegionId());
		Image thumbnailImage = imageService.findById(productSaveAndUpdateRequest.getImagesId().get(0));
		imageService.updateProductId(productSaveAndUpdateRequest.getImagesId(), product);
		product.updateFromDto(productSaveAndUpdateRequest, category, region, thumbnailImage);
	}

	@Transactional
	public void delete(Long productId) {
		productJpaRepository.deleteById(productId);
	}

	@Transactional
	public void updateStatus(Long productId, ProductUpdateRequest productUpdateRequest) {
		Product product = findById(productId);
		ProductStatus productStatus = ProductStatus.fromDescription(productUpdateRequest.getStatus());
		product.changeStatus(productStatus.getCode());
	}

	public Product findById(Long productId) {
		return productJpaRepository.findById(productId)
			.orElseThrow(() -> new CustomRuntimeException(ProductException.NOT_FOUND_PRODUCT));
	}
}
