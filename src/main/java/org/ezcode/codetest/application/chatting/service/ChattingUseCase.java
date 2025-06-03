package org.ezcode.codetest.application.chatting.service;

import java.util.List;
import java.util.Map;

import org.ezcode.codetest.application.chatting.dto.request.ChatRoomDeleteRequest;
import org.ezcode.codetest.application.chatting.dto.request.ChatRoomSaveRequest;
import org.ezcode.codetest.application.chatting.dto.request.ChatSaveRequest;
import org.ezcode.codetest.application.chatting.dto.response.ChatResponse;
import org.ezcode.codetest.application.chatting.dto.response.ChatRoomResponse;
import org.ezcode.codetest.application.chatting.port.cache.ChatRoomCacheService;
import org.ezcode.codetest.application.chatting.port.event.ChattingMessageService;
import org.ezcode.codetest.application.chatting.port.session.ChattingSessionService;
import org.ezcode.codetest.application.chatting.port.session.SessionChangedEvent;
import org.ezcode.codetest.application.chatting.port.cache.ChatRoomCache;
import org.ezcode.codetest.domain.chat.model.Chat;
import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.ezcode.codetest.domain.chat.service.ChattingDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChattingUseCase {

	private final UserDomainService userDomainService;
	private final ChattingDomainService chattingDomainService;
	private final ChattingMessageService messageService;
	private final ChattingSessionService sessionService;
	private final ChatRoomCacheService cacheService;

	@Transactional
	public void createChatRoom(ChatRoomSaveRequest request, String email) {

		User user = userDomainService.getUser(email);

		ChatRoom savedRoom = chattingDomainService.createChatRoom(request.toEntity(user));

		cacheService.addChatRoomToCache(ChatRoomCache.from(savedRoom));

		messageService.handleRoomChangeEvent(SessionChangedEvent.builder()
			.roomId(savedRoom.getId())
			.headCount(0L)
			.title(savedRoom.getTitle())
			.eventType("CREATE")
			.build());
	}

	@Transactional
	public void removeChatRoom(ChatRoomDeleteRequest request, String email) {

		User user = userDomainService.getUser(email);

		ChatRoom removedRoom = chattingDomainService.getChatRoom(request.roomId());

		chattingDomainService.isChatRoomOwner(removedRoom, user.getId());

		chattingDomainService.removeChatRoom(removedRoom);

		cacheService.removeChatRoomFromCache(ChatRoomCache.from(removedRoom));

		sessionService.removeRoom(request.roomId());

		messageService.handleRoomChangeEvent(SessionChangedEvent.builder()
			.roomId(removedRoom.getId())
			.title(removedRoom.getTitle())
			.headCount(0L)
			.eventType("DELETE")
			.build());
	}

	@Transactional
	public void getChatRoomList(String principalName) {

		List<ChatRoomCache> chatRooms = cacheService.getChatRoomsFromCache();

		if (chatRooms.isEmpty()) {
			chatRooms = chattingDomainService
				.getChatRoomList()
				.stream()
				.map(ChatRoomCache::from)
				.toList();
			cacheService.addChatRoomsToCache(chatRooms);
		}

		List<ChatRoomResponse> roomLists = chatRooms.stream()
			.map(room -> ChatRoomResponse.builder()
				.roomId(room.roomId())
				.title(room.title())
				.headCount(sessionService.viewSession(room.roomId()))
				.eventType("GET")
				.build())
			.toList();

		messageService.handleEnter(roomLists, principalName);
	}

	@Transactional
	public void sendChatting(ChatSaveRequest request, String email, Long roomId) {

		User user = userDomainService.getUser(email);

		ChatRoom chatRoom = chattingDomainService.getChatRoom(roomId);

		Chat chat = chattingDomainService.createChatting(request.toEntity(user, chatRoom));

		messageService.handleBroadCastChat(ChatResponse.from(chat), roomId);
	}

	@Transactional
	public void getChattingHistory(String sessionId, String principalName, String email, Long roomId) {

		User user = userDomainService.getUser(email);

		ChatRoom chatRoom = chattingDomainService.getChatRoom(roomId);

		List<ChatResponse> chatLists = chattingDomainService
			.getRoomChatting(roomId)
			.stream()
			.map(ChatResponse::from)
			.toList();

		Long headCount = sessionService.addSession(sessionId, roomId);

		messageService.handleRoomEnter(chatLists, principalName);

		messageService.handleRoomEnterAndLeftEvent(user.getNickname() + " 님이 입장했어요~!", roomId);

		messageService.handleRoomChangeEvent(ChatRoomResponse.builder()
			.roomId(chatRoom.getId())
			.title(chatRoom.getTitle())
			.eventType("UPDATE")
			.headCount(headCount)
			.build());
	}

	@Transactional
	public void leftChatRoom(String sessionId, String email, Long roomId) {

		User user = userDomainService.getUser(email);

		ChatRoom chatRoom = chattingDomainService.getChatRoom(roomId);

		Map<String, Long> roomData = sessionService.removeSession(sessionId);

		messageService.handleRoomEnterAndLeftEvent(user.getNickname() + " 님이 나가셨습니다~", roomId);

		messageService.handleRoomChangeEvent(ChatRoomResponse.builder()
			.roomId(chatRoom.getId())
			.title(chatRoom.getTitle())
			.eventType("UPDATE")
			.headCount(roomData.get("headCount"))
			.build());
	}
}
