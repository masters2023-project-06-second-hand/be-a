package com.codesquad.secondhand.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ChatException implements CustomException {

	INVALID_CHAT_ROOM_ID(HttpStatus.BAD_REQUEST, "찾을수 없는 채팅방 아이디 입니다.");

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
