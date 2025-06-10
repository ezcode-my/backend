package org.ezcode.codetest.application.chatting.service;

import java.util.List;
import java.util.Map;

import org.ezcode.codetest.application.chatting.dto.request.ChatRoomDeleteRequest;
import org.ezcode.codetest.application.chatting.dto.request.ChatRoomSaveRequest;
import org.ezcode.codetest.application.chatting.dto.request.ChatSaveRequest;
import org.ezcode.codetest.application.chatting.dto.response.ChatResponse;
import org.ezcode.codetest.application.chatting.dto.response.RoomChangedResponse;
import org.ezcode.codetest.application.chatting.port.cache.ChatRoomCacheService;
import org.ezcode.codetest.application.chatting.port.event.ChatEventService;
import org.ezcode.codetest.application.chatting.port.session.ChatSessionService;
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
	private final ChatEventService eventService;
	private final ChatSessionService sessionService;
	private final ChatRoomCacheService cacheService;

	private static final String EVENT_TYPE_CREATE = "CREATE";
	private static final String EVENT_TYPE_UPDATE = "UPDATE";
	private static final String EVENT_TYPE_DELETE = "DELETE";
	private static final String EVENT_TYPE_GET = "GET";

	@Transactional
	public void createChatRoom(ChatRoomSaveRequest request, String email) {

		User user = userDomainService.getUser(email);

		ChatRoom savedRoom = chattingDomainService.createChatRoom(request.toEntity(user));

		cacheService.addChatRoomToCache(ChatRoomCache.from(savedRoom));

		eventService.publishRoomChangeEvent(RoomChangedResponse.from(savedRoom, EVENT_TYPE_CREATE));
	}

	@Transactional
	public void removeChatRoom(ChatRoomDeleteRequest request, String email) {

		User user = userDomainService.getUser(email);

		ChatRoom removedRoom = chattingDomainService.getChatRoom(request.roomId());

		chattingDomainService.isChatRoomOwner(removedRoom, user.getId());

		chattingDomainService.removeChatRoom(removedRoom);

		cacheService.removeChatRoomFromCache(ChatRoomCache.from(removedRoom));

		sessionService.removeRoomSession(request.roomId());

		eventService.publishRoomChangeEvent(RoomChangedResponse.from(removedRoom, EVENT_TYPE_DELETE));
	}

	@Transactional
	public void getChatRoomList(String principalName, String sessionId) {

		List<ChatRoomCache> chatRooms = cacheService.getChatRoomsFromCache();

		if (chatRooms.isEmpty()) {
			chatRooms = chattingDomainService
				.getChatRoomList()
				.stream()
				.map(ChatRoomCache::from)
				.toList();
			cacheService.addChatRoomsToCache(chatRooms);
		}

		List<RoomChangedResponse> roomLists = chatRooms.stream()
			.map(room ->
				RoomChangedResponse.from(
					room,
					EVENT_TYPE_GET,
					sessionService.viewSessionCount(room.roomId())
				)
			)
			.toList();

		eventService.publishEnterEvent(roomLists, principalName, sessionId);
	}

	@Transactional
	public void sendChatting(ChatSaveRequest request, String email, Long roomId) {

		User user = userDomainService.getUser(email);

		ChatRoom chatRoom = chattingDomainService.getChatRoom(roomId);

		Chat chat = chattingDomainService.createChatting(request.toEntity(user, chatRoom));

		eventService.publishBroadCastChatEvent(ChatResponse.from(chat), roomId);
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

		Long headCount = sessionService.addSessionCount(sessionId, roomId);

		eventService.publishRoomEnterEvent(chatLists, principalName, sessionId);

		eventService.publishRoomEnterAndLeftEvent(user.getNickname() + " 님이 입장했어요~!", roomId);

		eventService.publishRoomChangeEvent(RoomChangedResponse.from(
				chatRoom,
				EVENT_TYPE_UPDATE,
				headCount
			)
		);
	}

	@Transactional
	public void leftChatRoom(String sessionId, String email, Long roomId) {

		User user = userDomainService.getUser(email);

		ChatRoom chatRoom = chattingDomainService.getChatRoom(roomId);

		Map<String, Long> roomData = sessionService.removeSessionCount(sessionId);

		eventService.publishRoomEnterAndLeftEvent(user.getNickname() + " 님이 나가셨습니다~", roomId);

		eventService.publishRoomChangeEvent(RoomChangedResponse.from(
				chatRoom,
				EVENT_TYPE_UPDATE,
				roomData.get("headCount")
			)
		);
	}
}
