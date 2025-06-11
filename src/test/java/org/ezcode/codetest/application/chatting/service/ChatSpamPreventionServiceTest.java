package org.ezcode.codetest.application.chatting.service;

import static org.mockito.Mockito.*;

import org.ezcode.codetest.application.chatting.port.event.ChatEventService;
import org.ezcode.codetest.application.chatting.port.session.ChatLimitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("채팅 스팸 방지 서비스 테스트")
class ChatSpamPreventionServiceTest {

	@InjectMocks
	private ChatSpamPreventionService chatSpamPreventionService;

	@Mock
	private ChatLimitService limitService;

	@Mock
	private ChatEventService eventService;

	private String TEMP_TEST_EMAIL;
	private String TEMP_TEST_NICKNAME;
	private Long TEMP_ROOM_ID;

	@BeforeEach
	void setUp() {

		TEMP_TEST_EMAIL = "test@unknown.com";
		TEMP_TEST_NICKNAME = "익명 닉네임";
		TEMP_ROOM_ID = 1L;

	}

	@Nested
	@DisplayName("채팅 스팸 방지 서비스 성공 테스트")
	class ChattingUseCaseSuccessTest {

		@Test
		@DisplayName("채팅 차단 확인 성공")
		void isChatBlocked() {

			//when
			chatSpamPreventionService.isChatBlocked(TEMP_TEST_EMAIL);

			//then
			verify(limitService).isBlocked(TEMP_TEST_EMAIL);
		}

		@Test
		@DisplayName("채팅 차단 성공")
		void applyChatBlock() {

			//when
			chatSpamPreventionService.applyChatBlock(TEMP_TEST_EMAIL, TEMP_TEST_NICKNAME, TEMP_ROOM_ID);

			//then
			verify(limitService).applyChatSpamPenalty(TEMP_TEST_EMAIL);
			verify(eventService).publishChatMessageBroadcastEvent(
				ChatMessageTemplate.SPAM_BLOCK.format(TEMP_TEST_NICKNAME, 30),
				TEMP_ROOM_ID);
		}

		@Test
		@DisplayName("10초 당 채팅 카운트 증가 및 확인 성공")
		void countChatsInLast10Seconds() {

			//when
			chatSpamPreventionService.countChatsInLast10Seconds(TEMP_TEST_EMAIL);

			//then
			verify(limitService).increaseChatCount(TEMP_TEST_EMAIL);
		}

	}

}