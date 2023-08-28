package com.codesquad.secondhand.domain.product.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequest {
	private String status;

	public ProductUpdateRequest(String status) {
		this.status = status;
	}
}
