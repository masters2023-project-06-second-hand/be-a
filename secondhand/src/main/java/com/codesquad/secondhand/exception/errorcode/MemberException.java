package com.codesquad.secondhand.exception.errorcode;

import org.springframework.http.HttpStatus;

public enum MemberException implements CustomException {
	MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 ID에 대한 회원을 찾을수 없습니다."),
	MEMBER_NICKNAME_EXIST(HttpStatus.BAD_REQUEST, "중복된 닉네임 입니다. 다시 입력해주세요."),
	MEMBER_REGION_DELETE_FAIlED(HttpStatus.BAD_REQUEST, "최소 하나의 지역이 남아있어야 합니다.");

	private final HttpStatus status;
	private final String message;

	MemberException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

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
