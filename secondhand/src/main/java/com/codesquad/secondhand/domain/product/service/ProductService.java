package com.codesquad.secondhand.domain.product.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.category.entity.Category;
import com.codesquad.secondhand.domain.category.service.CategoryQueryService;
import com.codesquad.secondhand.domain.chat.service.ChatQueryService;
import com.codesquad.secondhand.domain.image.service.ImageQueryService;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberQueryService;
import com.codesquad.secondhand.domain.product.dto.request.ProductSaveAndUpdateRequest;
import com.codesquad.secondhand.domain.product.dto.request.ProductUpdateRequest;
import com.codesquad.secondhand.domain.product.dto.response.ProductDetailResponse;
import com.codesquad.secondhand.domain.product.dto.response.ProductFindAllResponse;
import com.codesquad.secondhand.domain.product.dto.response.ProductResponse;
import com.codesquad.secondhand.domain.product.dto.response.ProductStatResponse;
import com.codesquad.secondhand.domain.product.entity.Image;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.product.utils.ProductStatus;
import com.codesquad.secondhand.domain.reaction.service.ReactionQueryService;
import com.codesquad.secondhand.domain.region.entity.Region;
import com.codesquad.secondhand.domain.region.service.RegionQueryService;
import com.codesquad.secondhand.redis.util.RedisUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductService {

	public static final long NO_VIEWS = 0L;
	private final ProductQueryService productQueryService;
	private final CategoryQueryService categoryQueryService;
	private final RegionQueryService regionQueryService;
	private final MemberQueryService memberQueryService;
	private final ImageQueryService imageQueryService;
	private final ReactionQueryService reactionQueryService;
	private final ChatQueryService chatQueryService;
	private final RedisUtil redisUtil;

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

	public ProductDetailResponse findDetail(Long productId, Long memberId) {
		if (memberId != null) {
			redisUtil.addViewCount(productId, memberId);
		}
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

	public ProductFindAllResponse findAll(Long regionId, Long categoryId, Pageable pageable) {
		//검증을 위한 메서드
		regionQueryService.findById(regionId);
		if (categoryId != null) {
			categoryQueryService.findById(categoryId);
		}

		Slice<Product> productSlice = productQueryService.findAll(regionId, categoryId, pageable);

		List<ProductResponse> products =
			productSlice.stream()
				.map(this::mapToProductResponse)
				.collect(Collectors.toUnmodifiableList());

		return ProductFindAllResponse.of(products, productSlice.hasNext(), productSlice.getNumber());
	}

	public List<ProductResponse> findSalesProducts(Long memberId, Integer statusId) {
		//validate 을 위한 호출
		if (statusId != null) {
			ProductStatus.fromCode(statusId);
		}
		Member member = memberQueryService.findById(memberId);
		return productQueryService.findSalesProduct(member, statusId).stream()
			.map(this::mapToProductResponse)
			.collect(Collectors.toUnmodifiableList());
	}

	private ProductResponse mapToProductResponse(Product product) {
		Long chattingCount = chatQueryService.countByProduct(product);
		Long reactionCount = reactionQueryService.countByProduct(product);
		return ProductResponse.of(product, reactionCount, chattingCount);
	}

	@Transactional
	public void updateProductId(List<Long> imagesId, Product product) {
		imagesId.stream()
			.map(imageId -> imageQueryService.findById(imageId))
			.forEach(imageFromDb -> imageFromDb.updateProduct(product));
	}

	public ProductStatResponse findStat(Long productId, Long memberId) {
		Product product = productQueryService.findById(productId);
		Long viewCount = redisUtil.getViewCount(productId) + product.getViewCount();
		Long reactionCount = reactionQueryService.countByProduct(product);
		Long chattingCount = chatQueryService.countByProduct(product);

		// memberId가 null이 아닐 때만 isLiked를 확인
		Boolean isLiked = (memberId != null) ? reactionQueryService.isLiked(memberId, product) : false;

		return ProductStatResponse.of(viewCount, reactionCount, chattingCount, isLiked);
	}

	@Scheduled(cron = "* */3 * * * ?")
	@Transactional
	public void applyViewCountToDB() {
		Set<String> productKeys = redisUtil.getCurrentProductKeys();

		for (String currentKey : productKeys) {
			String productIdStr = currentKey.split(":")[1];
			Long productId = Long.valueOf(productIdStr);

			Long viewCount = redisUtil.getViewCount(productId);

			if (!viewCount.equals(NO_VIEWS)) {
				productQueryService.applyViewCntToDB(productId, viewCount);
				redisUtil.copyCurrentToPreviousAndDeleteCurrent(currentKey, productId);
			}
		}
	}
}
