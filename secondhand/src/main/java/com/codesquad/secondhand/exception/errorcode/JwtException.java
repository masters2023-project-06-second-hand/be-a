package com.codesquad.secondhand.exception.errorcode;

import org.springframework.http.HttpStatus;

import com.codesquad.secondhand.exception.CustomRuntimeException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

public enum JwtException implements CustomException {

	EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "기한이 만료되었습니다."),
	MALFORMED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "잘못된 형식의 토큰입니다."),
	SIGNATURE_EXCEPTION(HttpStatus.UNAUTHORIZED, "올바른 키가 아닙니다."),
	ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.UNAUTHORIZED, "잘못된 값이 들어왔습니다."),
	REFRESH_TOKEN_NOT_FOUND_EXCEPTION(HttpStatus.UNAUTHORIZED, "DB에 Refresh token이 존재하지 않습니다."),
	BLACKLISTED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "블랙리스트에 등록된 토큰입니다."),
	MALFORMED_SIGN_UP_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 형태의 SignUpToken 입니다."),
	MISSING_HEADER_TOKEN(HttpStatus.UNAUTHORIZED, "Header에 토큰이 존재하지 않습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	JwtException(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public static CustomException from(RuntimeException e) {
		if (e.getClass().equals(ExpiredJwtException.class)) {
			return JwtException.EXPIRED_JWT_EXCEPTION;
		}
		if (e.getClass().equals(MalformedJwtException.class)) {
			return JwtException.MALFORMED_JWT_EXCEPTION;
		}
		if (e.getClass().equals(SignatureException.class)) {
			return JwtException.SIGNATURE_EXCEPTION;
		}
		if (e.getClass().equals(IllegalArgumentException.class)) {
			return JwtException.ILLEGAL_ARGUMENT_EXCEPTION;
		}
		return ((CustomRuntimeException)e).getCustomException();
	}

	@Override
	public HttpStatus httpStatus() {
		return httpStatus;
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
