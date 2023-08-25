package com.codesquad.secondhand.exception.errorcode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ProductException implements CustomException {

    INVALID_STATUS_CODE(HttpStatus.BAD_REQUEST, "잘못된 상품 상태 번호 입니다."),
    INVALID_STATUS_DESCRIPTION(HttpStatus.BAD_REQUEST, "잘못된 상품 상태 문자 입니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return null;
    }

    @Override
    public String errorMessage() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
