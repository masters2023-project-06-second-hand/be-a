package com.codesquad.secondhand.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FileUploadException implements CustomException {

	EMPTY_FILE(HttpStatus.BAD_REQUEST, "업로드 할 파일을 선택해주세요"),
	FILE_READING(HttpStatus.BAD_REQUEST, "파일 읽기에 실패했습니다.");

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
