package org.ezcode.codetest.application.chatting.service;

import java.util.List;
import java.util.Map;

import org.ezcode.codetest.application.chatting.dto.request.ChatRoomSaveRequest;
import org.ezcode.codetest.application.chatting.dto.request.ChatSaveRequest;
import org.ezcode.codetest.application.chatting.dto.response.ChatResponse;
import org.ezcode.codetest.application.chatting.dto.response.ChatRoomResponse;
import org.ezcode.codetest.application.chatting.port.message.MessageService;
import org.ezcode.codetest.application.chatting.port.session.SessionService;
import org.ezcode.codetest.domain.chat.model.Chat;
import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.ezcode.codetest.domain.chat.service.ChattingDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.enums.AuthType;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.ezcode.codetest.infrastructure.persitence.repository.user.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChattingUseCase {

	//private final UserDomainService userDomainService;
	//임시로 user jpa 사용중입니다.
	private final UserJpaRepository userRepository;
	private final ChattingDomainService chattingDomainService;
	private final MessageService messageService;
	private final SessionService sessionService;

	@Transactional
	public void createChatRoom(ChatRoomSaveRequest request, Long userId) {
		//
		// //User user = userDomainService.findOrElseThrow(userId);
		//
		// //User 임시 테스트용 생성
		// User user = User.builder()
		// 	.age(1)
		// 	.username("아무개")
		// 	.nickname("테스트계정이에요")
		// 	.email("chat27@naver.com")
		// 	.blogUrl("ddd")
		// 	.authType(AuthType.EMAIL)
		// 	.password("ddd")
		// 	.role(UserRole.USER)
		// 	.build();
		//
		// //테이블 연관관계 때문에 임시 테스트용으로 생성, 나중에 없앨거임
		// userRepository.save(user);
		//
		// chattingDomainService.createChatRoom(ChatRoom.builder()
		// 	.title(request.title())
		// 	.isDeleted(false)
		// 	.user(user)
		// 	.build());
		//
		// //테스트 채팅룸 용도 나중에 지울 예정
		// chattingDomainService.createChatRoom(ChatRoom.builder()
		// 	.title("감자방")
		// 	.isDeleted(false)
		// 	.user(user)
		// 	.build());
		//
		// //테스트 채팅룸 용도 나중에 지울 예정
		// chattingDomainService.createChatRoom(ChatRoom.builder()
		// 	.title("공부방")
		// 	.isDeleted(false)
		// 	.user(user)
		// 	.build());
	}

	@Transactional
	public void getChatRoomList(String principalName) {

		List<ChatRoomResponse> roomLists = chattingDomainService
			.getChatRoomList()
			.stream()
			.map(room -> ChatRoomResponse.builder()
				.roomId(room.getId())
				.title(room.getTitle())
				.headCount(sessionService.viewSession(room.getId()))
				.build())
			.toList();

		messageService.handleEnter(roomLists, principalName);

	}

	@Transactional
	public void sendChatting(ChatSaveRequest request, Long userId, Long roomId) {

		//User user = userDomainService.findOrElseThrow(userId);
		//나중에 UserDomainService 올라오면 변경예정
		User user = userRepository.findById(userId).get();

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
	public void getChattingHistory(String sessionId, String principalName, Long userId, Long roomId) {

		//User user = userDomainService.findOrElseThrow(userId);
		User user = userRepository.findById(userId).get();

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
	public void leftChatRoom(String sessionId, Long userId, Long roomId) {

		//User user = userDomainService.findOrElseThrow(userId);
		User user = userRepository.findById(userId).get();

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
