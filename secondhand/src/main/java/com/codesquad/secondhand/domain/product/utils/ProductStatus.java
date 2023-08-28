package com.codesquad.secondhand.domain.product.utils;

import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.ProductException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductStatus {
	ON_SALE(0, "판매중"),
	RESERVED(1, "예약중"),
	SOLD_OUT(2, "판매완료");

	private final int code;
	private final String description;

	public static ProductStatus fromCode(int code) {
		for (ProductStatus productStatus : ProductStatus.values()) {
			if (productStatus.getCode() == code) {
				return productStatus;
			}
		}
		throw new CustomRuntimeException(ProductException.INVALID_STATUS_CODE);
	}

	public static ProductStatus fromDescription(String description) {
		for (ProductStatus productStatus : ProductStatus.values()) {
			if (productStatus.getDescription().equals(description)) {
				return productStatus;
			}
		}
		throw new CustomRuntimeException(ProductException.INVALID_STATUS_DESCRIPTION);
	}
}
