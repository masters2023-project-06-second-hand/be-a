package com.codesquad.secondhand.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.codesquad.secondhand.exception.errorcode.CustomException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomRuntimeException extends RuntimeException {
	public static final String STATUS = "status";
	public static final String MESSAGE = "message";
	private final CustomException customException;

	public ResponseEntity<Map<String, String>> sendError() {
		Map<String, String> responseMap = new LinkedHashMap<>();
		responseMap.put(STATUS, customException.httpStatus().toString());
		responseMap.put(MESSAGE, customException.getErrorMessage());

		return ResponseEntity.status(customException.httpStatus())
			.body(responseMap);
	}
}
