package com.codesquad.secondhand.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RegionException implements CustomException {

	REGION_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 ID에 대한 지역을 찾을수 없습니다."),
	REGION_SIZE(HttpStatus.BAD_REQUEST,"지역을 추가 할 수 없습니다. 지역을 두개 이상 등록 할 수 없습니다."),
	REGION_DUPLICATED(HttpStatus.BAD_REQUEST,"지역을 추가 할 수 없습니다. 이미 선택한 지역입니다." ),
	MEMBER_REGION_NOT_ADDED(HttpStatus.BAD_REQUEST, "등록 되지 않은 회원과 지역입니다.");

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
