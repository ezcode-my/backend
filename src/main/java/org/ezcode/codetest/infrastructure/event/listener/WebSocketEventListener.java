package org.ezcode.codetest.infrastructure.event.listener;

import java.util.HashMap;
import java.util.Map;

import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.ezcode.codetest.domain.chat.service.ChattingDomainService;
import org.ezcode.codetest.infrastructure.event.service.StompMessageService;
import org.ezcode.codetest.infrastructure.session.service.RedisSessionCountService;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener implements ApplicationListener<SessionDisconnectEvent> {

	private final StompMessageService messageService;
	private final RedisSessionCountService sessionService;
	private final ChattingDomainService chattingDomainService;

	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {

		StompHeaderAccessor h = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = h.getSessionId();

		Map<String, Long> roomData = sessionService.removeSessionCount(sessionId);

		ChatRoom chatRoom = chattingDomainService.getChatRoom(roomData.get("roomId"));

		Map<String, Object> payload = new HashMap<>();
		payload.put("roomId", chatRoom.getId());
		payload.put("title", chatRoom.getTitle());
		payload.put("headCount", roomData.get("headCount"));

		messageService.handleRoomChangeEvent(payload);

	}
}
