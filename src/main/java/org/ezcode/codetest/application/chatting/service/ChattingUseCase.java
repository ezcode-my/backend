package org.ezcode.codetest.application.chatting.service;

import java.util.List;
import java.util.Map;

import org.ezcode.codetest.application.chatting.dto.request.ChatRoomSaveRequest;
import org.ezcode.codetest.application.chatting.dto.request.ChatSaveRequest;
import org.ezcode.codetest.application.chatting.dto.response.ChatResponse;
import org.ezcode.codetest.application.chatting.dto.response.ChatRoomResponse;
import org.ezcode.codetest.application.chatting.port.cache.CacheService;
import org.ezcode.codetest.application.chatting.port.message.MessageService;
import org.ezcode.codetest.application.chatting.port.session.SessionService;
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
	private final MessageService messageService;
	private final SessionService sessionService;
	private final CacheService cacheService;

	@Transactional
	public void createChatRoom(ChatRoomSaveRequest request, String email) {

		User user = userDomainService.getUser(email);

		chattingDomainService.createChatRoom(ChatRoom
			.builder()
			.title(request.title())
			.isDeleted(false)
			.user(user)
			.build());

		List<ChatRoom> roomLists = chattingDomainService.getChatRoomList();

		//TODO : 추후 이벤트 방식으로 저장할 예정 (afterCommit), 리스트 자료구조 쓰기
		cacheService.replaceChatRoomsCache(roomLists);
	}

	@Transactional
	public void getChatRoomList(String principalName) {

		List<ChatRoom> chatRooms = cacheService.getChatRoomsFromCache();

		if (chatRooms == null) {
			chatRooms = chattingDomainService.getChatRoomList();
			cacheService.replaceChatRoomsCache(chatRooms);
		}

		List<ChatRoomResponse> roomLists = chatRooms.stream()
			.map(room -> ChatRoomResponse.builder()
				.roomId(room.getId())
				.title(room.getTitle())
				.headCount(sessionService.viewSession(room.getId()))
				.build())
			.toList();

		messageService.handleEnter(roomLists, principalName);
	}

	@Transactional
	public void sendChatting(ChatSaveRequest request, String email, Long roomId) {

		User user = userDomainService.getUser(email);

		ChatRoom chatRoom = chattingDomainService.getChatRoom(roomId);

		Chat chat = chattingDomainService.createChatting(Chat.builder()
			.chatRoom(chatRoom)
			.user(user)
			.message(request.message())
			.build()
		);

		messageService.handleBroadCastChat(ChatResponse.from(chat), roomId);
	}

	@Transactional
	public void getChattingHistory(String sessionId, String principalName, String email, Long roomId) {

		User user = userDomainService.getUser(email);

		ChatRoom chatRoom = chattingDomainService.getChatRoom(roomId);

		Long headCount = sessionService.addSession(sessionId, roomId);

		List<ChatResponse> chatLists = chattingDomainService
			.getRoomChatting(roomId)
			.stream()
			.map(ChatResponse::from)
			.toList();

		messageService.handleRoomEnter(chatLists, principalName);

		messageService.handleRoomEnterAndLeftEvent(user.getNickname() + " 님이 입장했어요~!", roomId);

		messageService.handleRoomChangeEvent(ChatRoomResponse.builder()
			.roomId(chatRoom.getId())
			.title(chatRoom.getTitle())
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
			.headCount(roomData.get("headCount"))
			.build());
	}

}
