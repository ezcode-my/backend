package org.ezcode.codetest.domain.chat.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.ezcode.codetest.domain.chat.exception.ChattingException;
import org.ezcode.codetest.domain.chat.exception.ChattingExceptionCode;
import org.ezcode.codetest.domain.chat.model.Chat;
import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.ezcode.codetest.domain.chat.repository.ChatRepository;
import org.ezcode.codetest.domain.chat.repository.ChatRoomRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("채팅 도메인 서비스 테스트")
class ChattingDomainServiceTest {

	@InjectMocks
	private ChattingDomainService chattingDomainService;

	@Mock
	private ChatRepository chatRepository;

	@Mock
	private ChatRoomRepository chatRoomRepository;

	@Spy
	private ChatRoom chatRoom;

	private User user;
	private Chat chat;

	private static final Long TEST_ROOM_ID = 1L;
	private static final Long WRONG_USER_ID = 2L;
	private static final String TEMP_TITLE_1 = "임시 방 제목1";
	private static final String TEST_EMAIL = "test@unknow.com";
	private static final String TEMP_MESSAGE_1 = "임시 채팅 메시지";

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

		chatRoom = ChatRoom.builder()
			.title(TEMP_TITLE_1)
			.user(user)
			.isDeleted(false)
			.build();

		ReflectionTestUtils.setField(chatRoom, "id", TEST_ROOM_ID);

		chatRoom = spy(chatRoom);

		chat = Chat.builder()
			.user(user)
			.chatRoom(chatRoom)
			.message(TEMP_MESSAGE_1)
			.build();
	}

	@Nested
	@DisplayName("채팅 도메인 서비스 성공 테스트")
	class ChattingUseCaseSuccessTest {

		@Test
		@DisplayName("채팅룸 저장 성공")
		void createChatRoom() {

			// when
			chattingDomainService.createChatRoom(chatRoom);

			// then
			verify(chatRoomRepository).save(chatRoom);
		}

		@Test
		@DisplayName("채팅룸 삭제 성공")
		void removeChatRoom() {

			// when
			chattingDomainService.removeChatRoom(chatRoom);

			// then
			verify(chatRoomRepository).delete(chatRoom);
		}

		@Test
		@DisplayName("채팅룸 주인 확인 성공")
		void isChatRoomOwner() {

			// given
			doReturn(true).when(chatRoom).isOwner(user.getId());

			// when
			chattingDomainService.isChatRoomOwner(chatRoom, user.getId());

			// then
			assertDoesNotThrow(() ->
				chattingDomainService.isChatRoomOwner(chatRoom, user.getId())
			);
		}

		@Test
		@DisplayName("채팅룸 리스트 조회 성공")
		void getChatRoomList() {

			// when
			chattingDomainService.getChatRoomList();

			// then
			verify(chatRoomRepository).findAll();
		}

		@Test
		@DisplayName("채팅 저장 성공")
		void createChatting() {

			// when
			chattingDomainService.createChatting(chat);

			// then
			verify(chatRepository).save(chat);
		}

		@Test
		@DisplayName("채팅룸 단일 조회 성공")
		void getChatRoom() {

			// given
			given(chatRoomRepository.findChatRoom(TEST_ROOM_ID)).willReturn(Optional.of(chatRoom));

			// when
			chattingDomainService.getChatRoom(TEST_ROOM_ID);

			// then
			verify(chatRoomRepository).findChatRoom(TEST_ROOM_ID);
		}

		@Test
		@DisplayName("채팅룸 대화 내역 조회 성공")
		void getRoomChatting() {

			// when
			chattingDomainService.getRoomChatting(TEST_ROOM_ID);

			// then
			verify(chatRepository).findChatsFromLastHour(TEST_ROOM_ID);
		}

	}

	@Nested
	@DisplayName("채팅 도메인 서비스 실패 테스트")
	class ChattingUseCaseFailureTest {

		@Test
		@DisplayName("채팅룸 주인 확인 실패")
		void isChatRoomOwner() {

			// given
			doReturn(false).when(chatRoom).isOwner(WRONG_USER_ID);

			// when
			ChattingException exception = assertThrows(ChattingException.class,
				() -> chattingDomainService.isChatRoomOwner(chatRoom, WRONG_USER_ID));

			// then
			assertAll(
				() -> assertEquals(ChattingExceptionCode.CHATROOM_NOT_OWNER, exception.getResponseCode())
			);
		}

		@Test
		@DisplayName("채팅룸 단일 조회 실패")
		void getChatRoom() {

			// given
			given(chatRoomRepository.findChatRoom(TEST_ROOM_ID)).willReturn(Optional.empty());

			// when
			ChattingException exception = assertThrows(ChattingException.class,
				() -> chattingDomainService.getChatRoom(TEST_ROOM_ID));

			// then
			verify(chatRoomRepository).findChatRoom(TEST_ROOM_ID);

			assertAll(
				() -> assertEquals(ChattingExceptionCode.CHATTING_ROOM_NOT_FOUND, exception.getResponseCode())
			);
		}
	}
}