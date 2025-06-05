package org.ezcode.codetest.presentation.advice;

import org.ezcode.codetest.common.dto.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import lombok.NonNull;

@RestControllerAdvice
public class ResponseBodyWrapper implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType,
		@NonNull Class<? extends HttpMessageConverter<?>> converterType) {
		Class<?> declaringClass = returnType.getDeclaringClass();
		return declaringClass.isAnnotationPresent(RestController.class);
	}

	@Override
	public Object beforeBodyWrite(
		Object body,
		@NonNull MethodParameter returnType,
		@NonNull MediaType selectedContentType,
		@NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
		@NonNull ServerHttpRequest request,
		@NonNull ServerHttpResponse response
	) {

		String path = request.getURI().getPath();

		if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
			return body;
		}

		if (body instanceof CommonResponse<?>) {
			return body;
		}

		ResponseMessage rm = returnType.getMethodAnnotation(ResponseMessage.class);
		String message = (rm != null) ? rm.value() : "정상적으로 수행되었습니다.";

		HttpStatusCode status = response instanceof ServletServerHttpResponse servlet
			? HttpStatusCode.valueOf(servlet.getServletResponse().getStatus())
			: HttpStatusCode.valueOf(HttpStatus.OK.value());

		return CommonResponse.of(true, message, status.value(), body);
	}
}