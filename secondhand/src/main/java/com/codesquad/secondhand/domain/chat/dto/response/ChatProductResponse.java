package com.codesquad.secondhand.domain.chat.dto.response;

import com.codesquad.secondhand.domain.product.entity.Product;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatProductResponse {
	private String name;
	private Long price;
	private String thumbnailUrl;

	public static ChatProductResponse from(Product product) {
		return ChatProductResponse.builder()
			.name(product.getName())
			.price(product.getPrice())
			.thumbnailUrl(product.getThumbnailImage())
			.build();
	}
}
