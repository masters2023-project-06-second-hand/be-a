package com.codesquad.secondhand.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberException implements CustomException {
	MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 ID에 대한 회원을 찾을수 없습니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public HttpStatus httpStatus() {
		return status;
	}

	@Override
	public String errorMessage() {
		return message;
	}

	@Override
	public String getName() {
		return name();
	}
}
