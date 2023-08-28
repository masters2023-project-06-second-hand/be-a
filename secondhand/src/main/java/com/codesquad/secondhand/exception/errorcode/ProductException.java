package com.codesquad.secondhand.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductException implements CustomException {

	INVALID_STATUS_CODE(HttpStatus.BAD_REQUEST, "잘못된 상품 상태 번호 입니다."),
	INVALID_STATUS_DESCRIPTION(HttpStatus.BAD_REQUEST, "잘못된 상품 상태 문자 입니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public HttpStatus httpStatus() {
		return status;
	}

	@Override
	public Object getErrorMessage() {
		return message;
	}

	@Override
	public String getName() {
		return name();
	}
}
