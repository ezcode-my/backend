package org.ezcode.codetest.presentation.chattingmanagement.chatting.interceptor;

import java.util.Map;

import org.ezcode.codetest.application.chatting.service.ChatSpamPreventionService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatLimitInterceptor implements HandlerInterceptor {

	private final ChatSpamPreventionService spamPreventionService;

	@Override
	public boolean preHandle(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull Object handler) throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) return false;

		Long roomId = extractRoomId(request);

		AuthUser authUser = (AuthUser)authentication.getPrincipal();

		String email = authUser.getEmail();
		String nickName = authUser.getNickname();

		if (spamPreventionService.isChatBlocked(email)) {
			return false;
		}

		Long count = spamPreventionService.countChatsInLast10Seconds(email);

		if (count >= 11L) {
			spamPreventionService.applyChatBlock(email, nickName, roomId);
			return false;
		}

		return true;
	}

	private Long extractRoomId(HttpServletRequest request) {

		//TODO : Pathvariable Long 타입 변환 실패시 예외처리 필요
		@SuppressWarnings("unchecked")
		Map<String, String> pathVars =
			(Map<String, String>)request.getAttribute(
				HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		String roomIdStr = pathVars.get("roomId");

		return Long.parseLong(roomIdStr);
	}
}