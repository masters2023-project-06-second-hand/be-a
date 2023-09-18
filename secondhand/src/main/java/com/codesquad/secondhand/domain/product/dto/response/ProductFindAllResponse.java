package com.codesquad.secondhand.domain.product.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductFindAllResponse {

	private List<ProductResponse> products;
	private Boolean hasNext;
	private int page;

	public static ProductFindAllResponse of(List<ProductResponse> products, Boolean hasNext, int page) {
		return ProductFindAllResponse.builder()
			.products(products)
			.hasNext(hasNext)
			.page(page)
			.build();
	}
}
