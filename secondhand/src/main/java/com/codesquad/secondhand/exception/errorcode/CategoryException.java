package com.codesquad.secondhand.exception.errorcode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CategoryException implements CustomException{
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "선택한 카테고리를 찾을 수 없습니다.");


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
