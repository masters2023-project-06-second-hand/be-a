package com.codesquad.secondhand.domain.product.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.codesquad.secondhand.domain.product.entity.Image;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.product.utils.ProductStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDetailResponse {
	private Long id;
	private Writer writer;
	private List<Image> images;
	private String productName;
	private String categoryName;
	private String regionName;
	private LocalDateTime createdAt;
	private String status;
	private String content;
	private Long price;

	public static ProductDetailResponse from(Product product) {
		return ProductDetailResponse.builder()
			.id(product.getId())
			.writer(Writer.from(product.getMember()))
			.images(product.getImages())
			.productName(product.getName())
			.categoryName(product.getCategory().getName())
			.regionName(product.getRegion().getName())
			.createdAt(product.getCreatedAt())
			.status(ProductStatus.fromCode(product.getStatus()).getDescription())
			.content(product.getContent())
			.price(product.getPrice())
			.build();
	}
}
