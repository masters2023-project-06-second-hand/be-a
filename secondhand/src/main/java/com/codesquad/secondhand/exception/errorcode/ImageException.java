package com.codesquad.secondhand.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageException implements CustomException {
	IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 ID에 대한 이미지를 찾을수 없습니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public HttpStatus httpStatus() {
		return status;
	}

	@Override
	public String getErrorMessage() {
		return message;
	}

	@Override
	public String getName() {
		return name();
	}
}
