package org.ezcode.codetest.common.base.exception;

import org.ezcode.codetest.common.dto.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<CommonResponse<Void>> handleSseAuthorizationDenied(
		AuthorizationDeniedException ex,
		HttpServletRequest request,
		HttpServletResponse response
	) {

		String requestUri = request.getRequestURI();
		boolean isSseRequest = requestUri.contains("/api/problems/**") ||
			"text/event-stream".equals(request.getHeader("Accept"));

		if (isSseRequest) {
			if (response.isCommitted()) {
				log.warn("SSE 응답 커밋 후 AuthorizationDeniedException 발생: {}", requestUri);
				return null;
			}
			return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(CommonResponse.of(false, ex.getMessage(), 400, null));
		}
		throw ex;
	}
}
