package com.codesquad.secondhand.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * GlobalExceptionHandler 에서 처리하지 않는 예외응답 형태를 담기위한 클래스 (jwt filter 및 spring security 에서 사용)
 */
@Getter
public class ErrorResponse {
	private HttpStatus status;
	private Map<String, String> message;

	public ErrorResponse(HttpStatus status, Map<String, String> message) {
		this.status = status;
		this.message = message;
	}

}
