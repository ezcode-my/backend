package org.ezcode.codetest.infrastructure.event.listener;

import java.util.HashMap;
import java.util.Map;

import org.ezcode.codetest.application.chatting.port.session.RoomSessionInfo;
import org.ezcode.codetest.application.chatting.service.ChatMessageTemplate;
import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.ezcode.codetest.domain.chat.service.ChattingDomainService;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.ezcode.codetest.infrastructure.event.publisher.StompMessageService;
import org.ezcode.codetest.infrastructure.session.service.RedisSessionCountService;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener implements ApplicationListener<SessionDisconnectEvent> {

	private final StompMessageService messageService;
	private final RedisSessionCountService sessionService;
	private final ChattingDomainService chattingDomainService;
	private final UserDomainService userDomainService;

	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {

		StompHeaderAccessor header = StompHeaderAccessor.wrap(event.getMessage());

		Map<String, Object> attrs = header.getSessionAttributes();

		if (attrs == null) {
			return;
		}

		Boolean isChatWs = (Boolean)attrs.get("isChattingWebsocket");

		if (!Boolean.TRUE.equals(isChatWs)) {
			return;
		}

		try {
			String sessionId = header.getSessionId();

			String email = header.getUser().getName();
			String nickName = userDomainService.getUser(email).getNickname();

			RoomSessionInfo roomData = sessionService.removeSessionCount(sessionId);
			ChatRoom chatRoom = chattingDomainService.getChatRoom(roomData.roomId());

			Map<String, Object> payload = new HashMap<>();
			payload.put("roomId", chatRoom.getId());
			payload.put("title", chatRoom.getTitle());
			payload.put("headCount", roomData.headCount());
			payload.put("eventType", "UPDATE");

			messageService.handleChatRoomParticipantCountChange(payload);
			messageService.handleChatRoomEntryExitMessage(ChatMessageTemplate.CHAT_ROOM_LEFT.format(nickName),
				chatRoom.getId());
		} catch (Exception e) {
			log.info("SessionDisconnectEvent 채팅 세션 정리중 예외 발생", e);
		}
	}
}
