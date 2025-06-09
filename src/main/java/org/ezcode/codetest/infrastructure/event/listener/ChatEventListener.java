package org.ezcode.codetest.infrastructure.event.listener;

import org.ezcode.codetest.infrastructure.event.dto.BroadCastChatEvent;
import org.ezcode.codetest.infrastructure.event.dto.EnterEvent;
import org.ezcode.codetest.infrastructure.event.dto.RoomChangeEvent;
import org.ezcode.codetest.infrastructure.event.dto.RoomEnterAndLeftEvent;
import org.ezcode.codetest.infrastructure.event.dto.RoomEnterEvent;
import org.ezcode.codetest.infrastructure.event.service.StompMessageService;
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
	public void handleEnterEvent(EnterEvent<?> event) {

		try {
			messageService.handleEnter(event.roomData(), event.principalName(), event.sessionId());
		} catch (Exception e) {
			log.warn("사용자 입장 후, 채팅룸 전달 중 오류 발생", e);
		}
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleRoomEnterEvent(RoomEnterEvent<?> event) {

		try {
			messageService.handleRoomEnter(event.chatData(), event.principalName(), event.sessionId());
		} catch (Exception e) {
			log.warn("사용자 채팅방 입장 후, 채팅내역 전달 중 오류 발생", e);
		}
	}

	@TransactionalEventListener(
		phase = TransactionPhase.AFTER_COMMIT,
		fallbackExecution = true
	)
	public void handleBroadCastChatEvent(BroadCastChatEvent<?> event) {

		try {
			messageService.handleBroadCastChat(event.chatData(), event.roomId());
		} catch (Exception e) {
			log.warn("사용자 채팅 메시지 전달 중 오류 발생", e);
		}
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleRoomEnterAndLeftEvent(RoomEnterAndLeftEvent<?> event) {

		try {
			messageService.handleRoomEnterAndLeftEvent(event.messageData(), event.roomId());
		} catch (Exception e) {
			log.warn("사용자 입장/퇴장 메시지 전달 중 오류 발생", e);
		}
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleRoomChangeEvent(RoomChangeEvent<?> event) {

		try {
			messageService.handleRoomChangeEvent(event.roomData());
		} catch (Exception e) {
			log.warn("채팅룸 상태 변화 전달 중 오류 발생", e);
		}
	}
}
