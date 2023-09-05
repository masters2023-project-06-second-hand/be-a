package com.codesquad.secondhand.exception.handler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.codesquad.secondhand.exception.CustomRuntimeException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomRuntimeException.class)
	public ResponseEntity<Map<String, String>> customExceptionHandler(CustomRuntimeException e) {
		log.info("api 예외발생! errorType: " + e.getCustomException().getName() + " errorMessage: " + e.getCustomException()
			.getErrorMessage());
		return e.sendError();
	}

	/**
	 * MaxUploadSizeExceededException.class 예외는 Dispatcher Servlet 이후 요청 파싱 단계에서 발생한다.
	 * 즉 Controller 호출 전에 발생하는 예외이기 때문에 MaxUploadSizeExceededException 을 GlobalExceptionHandler 에서 잡는다.
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Map<String, String>> maxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		log.info("api 예외발생! errorType: " + e.getClass().getSimpleName() + " errorMessage: " + "최대 파일 업로드 용량 초과");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(Map.of("errorType", e.getClass().getSimpleName(), "errorMessage",
				"최대 파일 사이즈를 초과합니다."));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {
		List<ObjectError> objectErrors = e.getBindingResult().getAllErrors();

		String errorMessage = objectErrors.stream()
			.map(ObjectError::getDefaultMessage)
			.collect(Collectors.joining(","));

		Map<String, Object> responseMap = new LinkedHashMap<>();
		responseMap.put("status", HttpStatus.BAD_REQUEST.toString());
		responseMap.put("message", errorMessage.toString());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(responseMap);
	}

}
