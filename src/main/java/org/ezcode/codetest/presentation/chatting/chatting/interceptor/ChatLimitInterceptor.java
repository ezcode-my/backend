package org.ezcode.codetest.presentation.chatting.chatting.interceptor;

import java.util.Map;

import org.ezcode.codetest.application.chatting.service.ChatSpamPreventionService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatLimitInterceptor implements HandlerInterceptor {

	private final ChatSpamPreventionService spamPreventionService;
	private final static Long MAX_CHAT_COUNT_PER_10SEC = 11L;

	@Override
	public boolean preHandle(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull Object handler) throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null)
			return false;

		Long roomId = extractRoomId(request);

		AuthUser authUser = (AuthUser)authentication.getPrincipal();

		String email = authUser.getEmail();
		String nickName = authUser.getNickname();

		if (spamPreventionService.isChatBlocked(email)) {
			return false;
		}

		Long count = spamPreventionService.countChatsInLast10Seconds(email);

		if (count >= MAX_CHAT_COUNT_PER_10SEC) {
			spamPreventionService.applyChatBlock(email, nickName, roomId);
			return false;
		}

		return true;
	}

	private Long extractRoomId(HttpServletRequest request) {

		try {
			@SuppressWarnings("unchecked")
			Map<String, String> pathVars =
				(Map<String, String>)request.getAttribute(
					HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

			String roomIdStr = pathVars.get("roomId");

			return Long.parseLong(roomIdStr);
		} catch (Exception e) {
			log.warn("Pathvariable 입력 값 검증 실패");
			throw new NumberFormatException(e.getMessage());
		}
	}
}