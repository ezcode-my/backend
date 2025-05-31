package org.ezcode.codetest.presentation.usermanagement.resolver;

import org.ezcode.codetest.common.annotation.Auth;
import org.ezcode.codetest.common.dto.AuthUser;
import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasAuthAnnotation = parameter.getParameterAnnotation(Auth.class)!= null;
		boolean isAuthUserType = parameter.getParameterType().equals(AuthUser.class);

		if (hasAuthAnnotation != isAuthUserType) {
			throw new AuthException("@Auth와 AuthUser타입은 함께 사용되어야합니다");
		}
		return hasAuthAnnotation;
	}


	@Override
	public Object resolveArgument(
			@Nullable MethodParameter parameter,
			@Nullable ModelAndViewContainer modelAndViewContainer,
			NativeWebRequest webRequest,
			@Nullable WebDataBinderFactory webDataBinderFactory
	){
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

		Long userId = (Long) request.getSession().getAttribute("userId");
		String email = (String) request.getSession().getAttribute("email");
		String username = (String) request.getSession().getAttribute("username");
		String nickName = (String) request.getSession().getAttribute("nickName");
		UserRole userRole = (UserRole) request.getSession().getAttribute("userRole");

		return new AuthUser(userId, email, username, nickName, userRole);
	}
}
