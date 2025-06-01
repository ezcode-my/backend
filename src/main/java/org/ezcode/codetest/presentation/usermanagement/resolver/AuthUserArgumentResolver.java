package org.ezcode.codetest.presentation.usermanagement.resolver;

import org.ezcode.codetest.common.annotation.Auth;
import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		log.info("parameter type: {}", parameter.getParameterType().getName());
		log.info("AuthUser class: {}", AuthUser.class.getName());
		log.info("AuthUserArgumentResolver 진입, 같은지 비교, {} : {}",parameter.hasParameterAnnotation(Auth.class), parameter.getParameterType().equals(AuthUser.class));
		return parameter.hasParameterAnnotation(Auth.class) &&
			parameter.getParameterType().equals(AuthUser.class);
	}


	@Override
	public Object resolveArgument(
			MethodParameter parameter,
			ModelAndViewContainer modelAndViewContainer,
			NativeWebRequest webRequest,
			 WebDataBinderFactory webDataBinderFactory
	){
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		log.info(">>> AuthUserArgumentResolver 호출됨, authUser = {}", request.getAttribute("authUser"));

		AuthUser authUser = (AuthUser) request.getAttribute("authUser");


		if (authUser == null) {
			throw new AuthException("인증 정보가 없습니다");
		}

		return authUser;
	}
}
