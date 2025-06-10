package org.ezcode.codetest.application.chatting.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.ezcode.codetest.application.chatting.dto.request.ChatRoomDeleteRequest;
import org.ezcode.codetest.application.chatting.dto.request.ChatRoomSaveRequest;
import org.ezcode.codetest.application.chatting.dto.request.ChatSaveRequest;
import org.ezcode.codetest.application.chatting.dto.response.ChatResponse;
import org.ezcode.codetest.application.chatting.dto.response.RoomChangedResponse;
import org.ezcode.codetest.application.chatting.port.cache.ChatRoomCache;
import org.ezcode.codetest.application.chatting.port.cache.ChatRoomCacheService;
import org.ezcode.codetest.application.chatting.port.event.ChatEventService;
import org.ezcode.codetest.application.chatting.port.session.ChatSessionService;
import org.ezcode.codetest.domain.chat.model.Chat;
import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.ezcode.codetest.domain.chat.service.ChattingDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("채팅 서비스 테스트")
class ChattingUseCaseTest {

	private static final String TEST_EMAIL      = "test@unknow.com";
	private static final String TEST_PRINCIPAL  = "익명 Principal";
	private static final String TEST_SESSION_ID = "임시 세션 ID";
	private static final Long   TEST_ROOM_ID    = 1L;
	private static final String TEMP_TITLE_1    = "임시 방 제목1";
	private static final String TEMP_TITLE_2    = "임시 방 제목2";
	private static final String TEMP_MESSAGE_1  = "임시 채팅 메시지";
	private static final String TEMP_MESSAGE_2  = "임시 채팅 메시지2";
	private static final String EVENT_CREATE    = "CREATE";
	private static final String EVENT_DELETE    = "DELETE";
	private static final String EVENT_GET       = "GET";
	private static final String EVENT_UPDATE    = "UPDATE";

	@InjectMocks
	private ChattingUseCase chattingUseCase;

	@Mock
	private UserDomainService     userDomainService;
	@Mock
	private ChattingDomainService chattingDomainService;
	@Mock
	private ChatEventService      eventService;
	@Mock
	private ChatSessionService    sessionService;
	@Mock
	private ChatRoomCacheService  cacheService;

	private User                user;
	private Chat                chat1;
	private ChatRoom            chatRoom1;
	private List<ChatRoomCache> cacheChatRooms;
	private List<ChatRoom>      chatRooms;
	private List<Chat>          chats;

	@BeforeEach
	void setUp() {

		user = User.builder()
			.email(TEST_EMAIL)
			.username("익명")
			.password("P@ssw0rd1225")
			.nickname("익명 닉네임")
			.tier(Tier.NEWBIE)
			.age(22)
			.build();
		ReflectionTestUtils.setField(user, "id", TEST_ROOM_ID);

		chatRoom1 = ChatRoom.builder()
			.title(TEMP_TITLE_1)
			.isDeleted(false)
			.user(user)
			.build();
		ChatRoom chatRoom2 = ChatRoom.builder()
			.title(TEMP_TITLE_2)
			.isDeleted(false)
			.user(user)
			.build();
		ReflectionTestUtils.setField(chatRoom1, "id", TEST_ROOM_ID);
		ReflectionTestUtils.setField(chatRoom2, "id", 2L);

		cacheChatRooms = Stream.of(chatRoom1, chatRoom2)
			.map(ChatRoomCache::from)
			.toList();
		chatRooms = List.of(chatRoom1, chatRoom2);

		chat1 = Chat.builder()
			.user(user)
			.chatRoom(chatRoom1)
			.message(TEMP_MESSAGE_1)
			.build();
		Chat chat2 = Chat.builder()
			.user(user)
			.chatRoom(chatRoom1)
			.message(TEMP_MESSAGE_2)
			.build();
		chats = List.of(chat1, chat2);
	}

	@Nested
	@DisplayName("채팅 서비스 성공 테스트")
	class ChattingUseCaseSuccessTest {

		@Test
		@DisplayName("채팅룸 생성 성공")
		void createChatRoom() {

			// given
			ChatRoomSaveRequest request = new ChatRoomSaveRequest(TEMP_TITLE_1);
			given(userDomainService.getUser(TEST_EMAIL)).willReturn(user);
			given(chattingDomainService.createChatRoom(any(ChatRoom.class)))
				.willReturn(chatRoom1);

			// when
			chattingUseCase.createChatRoom(request, TEST_EMAIL);

			// then
			verify(userDomainService).getUser(TEST_EMAIL);
			ArgumentCaptor<ChatRoom> captor = ArgumentCaptor.forClass(ChatRoom.class);
			verify(chattingDomainService).createChatRoom(captor.capture());
			ChatRoom passed = captor.getValue();

			assertAll(
				() -> assertEquals(TEMP_TITLE_1, passed.getTitle()),
				() -> assertEquals(user, passed.getUser())
			);

			verify(cacheService).addChatRoomToCache(ChatRoomCache.from(chatRoom1));
			verify(eventService).publishRoomChangeEvent(
				RoomChangedResponse.from(chatRoom1, EVENT_CREATE)
			);
		}

		@Test
		@DisplayName("채팅룸 삭제 성공")
		void removeChatRoom() {

			// given
			ChatRoomDeleteRequest request = new ChatRoomDeleteRequest(TEST_ROOM_ID);
			given(userDomainService.getUser(TEST_EMAIL)).willReturn(user);
			given(chattingDomainService.getChatRoom(TEST_ROOM_ID))
				.willReturn(chatRoom1);

			// when
			chattingUseCase.removeChatRoom(request, TEST_EMAIL);

			// then
			verify(userDomainService).getUser(TEST_EMAIL);
			verify(chattingDomainService).getChatRoom(TEST_ROOM_ID);
			verify(chattingDomainService).isChatRoomOwner(chatRoom1, user.getId());
			verify(chattingDomainService).removeChatRoom(chatRoom1);

			ArgumentCaptor<ChatRoomCache> cacheCaptor = ArgumentCaptor.forClass(ChatRoomCache.class);
			verify(cacheService).removeChatRoomFromCache(cacheCaptor.capture());
			ChatRoomCache cache = cacheCaptor.getValue();
			assertAll(
				() -> assertEquals(TEST_ROOM_ID, cache.roomId()),
				() -> assertEquals(TEMP_TITLE_1, cache.title())
			);

			verify(sessionService).removeRoomSession(TEST_ROOM_ID);

			ArgumentCaptor<RoomChangedResponse> responseCaptor = ArgumentCaptor.forClass(RoomChangedResponse.class);
			verify(eventService).publishRoomChangeEvent(responseCaptor.capture());
			RoomChangedResponse response = responseCaptor.getValue();
			assertAll(
				() -> assertEquals(TEST_ROOM_ID, response.roomId()),
				() -> assertEquals(TEMP_TITLE_1, response.title()),
				() -> assertEquals(EVENT_DELETE, response.eventType())
			);
		}

		@Test
		@DisplayName("채팅룸 불러오기 성공(캐시에서)")
		void getChatRoomListFromCache() {

			// given
			given(cacheService.getChatRoomsFromCache()).willReturn(cacheChatRooms);
			given(sessionService.viewSessionCount(anyLong())).willReturn(1L);

			// when
			chattingUseCase.getChatRoomList(TEST_PRINCIPAL, TEST_SESSION_ID);

			// then
			verify(cacheService).getChatRoomsFromCache();
			verify(sessionService, times(cacheChatRooms.size()))
				.viewSessionCount(anyLong());

			@SuppressWarnings("unchecked")
			ArgumentCaptor<List<RoomChangedResponse>> captor =
				(ArgumentCaptor<List<RoomChangedResponse>>)(Object)
					ArgumentCaptor.forClass(List.class);
			verify(eventService).publishEnterEvent(captor.capture(), eq(TEST_PRINCIPAL), eq(TEST_SESSION_ID));

			List<RoomChangedResponse> published = captor.getValue();
			assertEquals(cacheChatRooms.size(), published.size());
			for (int i = 0; i < cacheChatRooms.size(); i++) {
				assertEquals(cacheChatRooms.get(i).roomId(), published.get(i).roomId());
				assertEquals(EVENT_GET, published.get(i).eventType());
				assertEquals(1L, published.get(i).headCount());
			}
		}

		@Test
		@DisplayName("채팅룸 불러오기 성공(DB에서)")
		void getChatRoomListFromDB() {

			// given
			given(cacheService.getChatRoomsFromCache()).willReturn(List.of());
			given(chattingDomainService.getChatRoomList()).willReturn(chatRooms);
			given(sessionService.viewSessionCount(anyLong())).willReturn(1L);

			// when
			chattingUseCase.getChatRoomList(TEST_PRINCIPAL, TEST_SESSION_ID);

			// then
			verify(chattingDomainService).getChatRoomList();

			@SuppressWarnings("unchecked")
			ArgumentCaptor<List<ChatRoomCache>> chatRoomsCaptor =
				(ArgumentCaptor<List<ChatRoomCache>>)(Object)
					ArgumentCaptor.forClass(List.class);
			verify(cacheService).addChatRoomsToCache(chatRoomsCaptor.capture());
			List<ChatRoomCache> capturedRooms = chatRoomsCaptor.getValue();
			assertEquals(chatRooms.size(), capturedRooms.size());

			verify(sessionService, times(capturedRooms.size()))
				.viewSessionCount(anyLong());

			@SuppressWarnings("unchecked")
			ArgumentCaptor<List<RoomChangedResponse>> responseCaptor =
				(ArgumentCaptor<List<RoomChangedResponse>>)(Object)
					ArgumentCaptor.forClass(List.class);
			verify(eventService).publishEnterEvent(responseCaptor.capture(), eq(TEST_PRINCIPAL), eq(TEST_SESSION_ID));

			List<RoomChangedResponse> published = responseCaptor.getValue();
			assertEquals(capturedRooms.size(), published.size());
			for (int i = 0; i < capturedRooms.size(); i++) {
				assertEquals(capturedRooms.get(i).roomId(), published.get(i).roomId());
				assertEquals(EVENT_GET, published.get(i).eventType());
				assertEquals(1L, published.get(i).headCount());
			}
		}

		@Test
		@DisplayName("채팅 메시지 보내기 성공")
		void sendChatting() {

			// given
			ChatSaveRequest request = new ChatSaveRequest(TEMP_MESSAGE_1);
			given(userDomainService.getUser(anyString())).willReturn(user);
			given(chattingDomainService.getChatRoom(anyLong())).willReturn(chatRoom1);
			given(chattingDomainService.createChatting(any())).willReturn(chat1);

			// when
			chattingUseCase.sendChatting(request, TEST_EMAIL, TEST_ROOM_ID);

			// then
			verify(userDomainService).getUser(TEST_EMAIL);
			verify(chattingDomainService).getChatRoom(TEST_ROOM_ID);

			ArgumentCaptor<Chat> chatCaptor = ArgumentCaptor.forClass(Chat.class);
			verify(chattingDomainService).createChatting(chatCaptor.capture());
			Chat passedChat = chatCaptor.getValue();
			assertAll(
				() -> assertEquals(chatRoom1, passedChat.getChatRoom()),
				() -> assertEquals(user, passedChat.getUser()),
				() -> assertEquals(request.message(), passedChat.getMessage())
			);

			ArgumentCaptor<ChatResponse> responseCaptor = ArgumentCaptor.forClass(ChatResponse.class);
			verify(eventService).publishBroadCastChatEvent(responseCaptor.capture(), eq(TEST_ROOM_ID));
			ChatResponse passedResponse = responseCaptor.getValue();
			assertAll(
				() -> assertEquals(passedResponse.message(), passedChat.getMessage()),
				() -> assertEquals(passedResponse.name(), user.getNickname())
			);
		}

		@Test
		@DisplayName("채팅 로그 읽어오기 성공")
		void getChattingHistory() {

			// given
			given(userDomainService.getUser(anyString())).willReturn(user);
			given(chattingDomainService.getChatRoom(anyLong())).willReturn(chatRoom1);
			given(chattingDomainService.getRoomChatting(anyLong())).willReturn(chats);
			given(sessionService.addSessionCount(anyString(), anyLong())).willReturn(1L);

			// when
			chattingUseCase.getChattingHistory(TEST_SESSION_ID, TEST_PRINCIPAL, TEST_EMAIL, TEST_ROOM_ID);

			// then
			verify(userDomainService).getUser(TEST_EMAIL);
			verify(chattingDomainService).getChatRoom(TEST_ROOM_ID);
			verify(chattingDomainService).getRoomChatting(TEST_ROOM_ID);
			verify(sessionService).addSessionCount(TEST_SESSION_ID, TEST_ROOM_ID);

			@SuppressWarnings("unchecked")
			ArgumentCaptor<List<ChatResponse>> chatResponseCaptor =
				(ArgumentCaptor<List<ChatResponse>>)(Object)
					ArgumentCaptor.forClass(List.class);
			verify(eventService).publishRoomEnterEvent(chatResponseCaptor.capture(), eq(TEST_PRINCIPAL), eq(TEST_SESSION_ID));

			List<ChatResponse> passedChatResponse = chatResponseCaptor.getValue();
			passedChatResponse.forEach(chatResponse ->
				assertEquals(chatResponse.name(), user.getNickname())
			);

			verify(eventService).publishRoomEnterAndLeftEvent(user.getNickname() + " 님이 입장했어요~!", TEST_ROOM_ID);

			ArgumentCaptor<RoomChangedResponse> roomChangedResponseCaptor = ArgumentCaptor.forClass(RoomChangedResponse.class);
			verify(eventService).publishRoomChangeEvent(roomChangedResponseCaptor.capture());
			RoomChangedResponse passedRoomChangedResponse = roomChangedResponseCaptor.getValue();
			assertAll(
				() -> assertEquals(EVENT_UPDATE, passedRoomChangedResponse.eventType()),
				() -> assertEquals(passedRoomChangedResponse.title(), chatRoom1.getTitle()),
				() -> assertEquals(passedRoomChangedResponse.roomId(), chatRoom1.getId()),
				() -> assertEquals(1L, passedRoomChangedResponse.headCount())
			);
		}

		@Test
		@DisplayName("채팅방 나가기 성공")
		void leftChatRoom() {

			// given
			given(userDomainService.getUser(anyString())).willReturn(user);
			given(chattingDomainService.getChatRoom(anyLong())).willReturn(chatRoom1);
			given(sessionService.removeSessionCount(TEST_SESSION_ID))
				.willReturn(Map.of("roomId", TEST_ROOM_ID, "headCount", 1L));

			// when
			chattingUseCase.leftChatRoom(TEST_SESSION_ID, TEST_EMAIL, TEST_ROOM_ID);

			// then
			verify(userDomainService).getUser(TEST_EMAIL);
			verify(chattingDomainService).getChatRoom(TEST_ROOM_ID);

			verify(eventService).publishRoomEnterAndLeftEvent(user.getNickname() + " 님이 나가셨습니다~", TEST_ROOM_ID);

			ArgumentCaptor<RoomChangedResponse> responseCaptor = ArgumentCaptor.forClass(RoomChangedResponse.class);
			verify(eventService).publishRoomChangeEvent(responseCaptor.capture());
			RoomChangedResponse passed = responseCaptor.getValue();
			assertAll(
				() -> assertEquals(TEST_ROOM_ID, passed.roomId()),
				() -> assertEquals(TEMP_TITLE_1, passed.title()),
				() -> assertEquals("UPDATE", passed.eventType()),
				() -> assertEquals(1L, passed.headCount())
			);
		}
	}
}
