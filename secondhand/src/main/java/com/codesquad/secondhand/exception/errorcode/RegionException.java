package com.codesquad.secondhand.exception.errorcode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum RegionException implements CustomException{

    REGION_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 ID에 대한 지역을 찾을수 없습니다.");

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
