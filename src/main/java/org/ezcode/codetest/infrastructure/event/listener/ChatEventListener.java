package org.ezcode.codetest.infrastructure.event.listener;

import org.ezcode.codetest.infrastructure.event.dto.ChatMessageBroadcastEvent;
import org.ezcode.codetest.infrastructure.event.dto.ChatRoomListLoadEvent;
import org.ezcode.codetest.infrastructure.event.dto.ChatRoomParticipantCountChangeEvent;
import org.ezcode.codetest.infrastructure.event.dto.ChatRoomEntryExitMessageEvent;
import org.ezcode.codetest.infrastructure.event.dto.ChatRoomHistoryLoadEvent;
import org.ezcode.codetest.infrastructure.event.publisher.StompMessageService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatEventListener {

	private final StompMessageService messageService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleChatRoomListLoad(ChatRoomListLoadEvent<?> event) {

		try {
			messageService.handleChatRoomListLoad(event.roomData(), event.principalName(), event.sessionId());
		} catch (Exception e) {
			log.warn("사용자 입장 후, 채팅룸 전달 중 오류 발생", e);
		}
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleChatRoomHistoryLoad(ChatRoomHistoryLoadEvent<?> event) {

		try {
			messageService.handleChatRoomHistoryLoad(event.chatData(), event.principalName(), event.sessionId());
		} catch (Exception e) {
			log.warn("사용자 채팅방 입장 후, 채팅내역 전달 중 오류 발생", e);
		}
	}

	@TransactionalEventListener(
		phase = TransactionPhase.AFTER_COMMIT,
		fallbackExecution = true
	)
	public void handleChatMessageBroadcast(ChatMessageBroadcastEvent<?> event) {

		try {
			messageService.handleChatMessageBroadcast(event.chatData(), event.roomId());
		} catch (Exception e) {
			log.warn("사용자 채팅 메시지 전달 중 오류 발생", e);
		}
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleChatRoomEntryExitMessage(ChatRoomEntryExitMessageEvent<?> event) {

		try {
			messageService.handleChatRoomEntryExitMessage(event.messageData(), event.roomId());
		} catch (Exception e) {
			log.warn("사용자 입장/퇴장 메시지 전달 중 오류 발생", e);
		}
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleChatRoomParticipantCountChange(ChatRoomParticipantCountChangeEvent<?> event) {

		try {
			messageService.handleChatRoomParticipantCountChange(event.roomData());
		} catch (Exception e) {
			log.warn("채팅룸 상태 변화 전달 중 오류 발생", e);
		}
	}
}
