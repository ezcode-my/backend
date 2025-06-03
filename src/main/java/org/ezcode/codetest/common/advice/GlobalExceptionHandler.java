package org.ezcode.codetest.common.advice;

import org.ezcode.codetest.common.base.exception.BaseException;
import org.ezcode.codetest.common.dto.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CommonResponse<Void>> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e,
		HttpServletRequest request
	) {
		String errorMessage = e.getBindingResult().getFieldErrors().stream()
			.findFirst()
			.map(FieldError::getDefaultMessage)
			.orElse("잘못된 요청입니다.");

		return ResponseEntity
			.badRequest()
			.body(CommonResponse.of(false, errorMessage, 400, null));
	}

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<CommonResponse<Void>> handleBaseException(
		BaseException e
	) {
		return ResponseEntity
			.status(e.getHttpStatus())
			.body(CommonResponse.from(e.getResponseCode()));
	}
}
