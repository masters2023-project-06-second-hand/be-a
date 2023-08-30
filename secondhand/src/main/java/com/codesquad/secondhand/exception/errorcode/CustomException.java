package com.codesquad.secondhand.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface CustomException {
	HttpStatus httpStatus();

	String getErrorMessage();

	String getName();
}
