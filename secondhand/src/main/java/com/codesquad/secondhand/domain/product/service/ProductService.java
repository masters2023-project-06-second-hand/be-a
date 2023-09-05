package com.codesquad.secondhand.domain.product.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.category.entity.Category;
import com.codesquad.secondhand.domain.category.service.CategoryQueryService;
import com.codesquad.secondhand.domain.image.service.ImageQueryService;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberQueryService;
import com.codesquad.secondhand.domain.product.dto.request.ProductSaveAndUpdateRequest;
import com.codesquad.secondhand.domain.product.dto.request.ProductUpdateRequest;
import com.codesquad.secondhand.domain.product.dto.response.ProductDetailResponse;
import com.codesquad.secondhand.domain.product.dto.response.ProductFindAllResponse;
import com.codesquad.secondhand.domain.product.entity.Image;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.product.utils.ProductStatus;
import com.codesquad.secondhand.domain.reaction.service.ReactionQueryService;
import com.codesquad.secondhand.domain.region.entity.Region;
import com.codesquad.secondhand.domain.region.service.RegionQueryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductService {

	private final ProductQueryService productQueryService;
	private final CategoryQueryService categoryQueryService;
	private final RegionQueryService regionQueryService;
	private final MemberQueryService memberQueryService;
	private final ImageQueryService imageQueryService;
	private final ReactionQueryService reactionQueryService;

	@Transactional
	public Long save(ProductSaveAndUpdateRequest productSaveAndUpdateRequest, Long memberId) {
		Category category = categoryQueryService.findById(productSaveAndUpdateRequest.getCategoryId());
		Region region = regionQueryService.findById(productSaveAndUpdateRequest.getRegionId());
		Member member = memberQueryService.findById(memberId);
		Image thumbnailImage = imageQueryService.findById(productSaveAndUpdateRequest.getImagesId().get(0));
		Product product = productSaveAndUpdateRequest.toEntity(category, region, member, thumbnailImage);
		updateProductId(productSaveAndUpdateRequest.getImagesId(), product);

		return productQueryService.save(product);
	}

	public ProductDetailResponse findDetail(Long productId) {
		return ProductDetailResponse.from(productQueryService.findById(productId));
	}

	@Transactional
	public void update(Long productId, ProductSaveAndUpdateRequest productSaveAndUpdateRequest) {
		Product product = productQueryService.findById(productId);
		Category category = categoryQueryService.findById(productSaveAndUpdateRequest.getCategoryId());
		Region region = regionQueryService.findById(productSaveAndUpdateRequest.getRegionId());
		Image thumbnailImage = imageQueryService.findById(productSaveAndUpdateRequest.getImagesId().get(0));
		updateProductId(productSaveAndUpdateRequest.getImagesId(), product);
		product.updateFromDto(productSaveAndUpdateRequest, category, region, thumbnailImage);
	}

	@Transactional
	public void delete(Long productId) {
		productQueryService.deleteById(productId);
	}

	@Transactional
	public void updateStatus(Long productId, ProductUpdateRequest productUpdateRequest) {
		Product product = productQueryService.findById(productId);
		ProductStatus productStatus = ProductStatus.fromDescription(productUpdateRequest.getStatus());
		product.changeStatus(productStatus.getCode());
	}

	public List<ProductFindAllResponse> findAll(Long regionId, Long categoryId) {
		//검증을 위한 메서드
		regionQueryService.findById(regionId);
		if (categoryId != null) {
			categoryQueryService.findById(categoryId);
		}
		return productQueryService.findAll(regionId, categoryId).stream()
			.map(this::mapToProductFindAllResponse)
			.collect(Collectors.toUnmodifiableList());
	}

	public List<ProductFindAllResponse> findSalesProducts(Long memberId, Integer statusId) {
		//validate 을 위한 호출
		if (statusId != null) {
			ProductStatus.fromCode(statusId);
		}
		Member member = memberQueryService.findById(memberId);
		return productQueryService.findSalesProduct(member, statusId).stream()
			.map(this::mapToProductFindAllResponse)
			.collect(Collectors.toUnmodifiableList());
	}

	private ProductFindAllResponse mapToProductFindAllResponse(Product product) {
		long reactionCount = reactionQueryService.countByProduct(product);
		return ProductFindAllResponse.of(product, reactionCount);
	}

	@Transactional
	public void updateProductId(List<Long> imagesId, Product product) {
		imagesId.stream()
			.map(imageId -> imageQueryService.findById(imageId))
			.forEach(imageFromDb -> imageFromDb.updateProduct(product));
	}
}
