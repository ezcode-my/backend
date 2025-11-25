package org.ezcode.codetest.infrastructure.notification;

import static org.assertj.core.api.Assertions.*;
import static org.ezcode.codetest.infrastructure.notification.model.NotificationQueueConstants.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.awaitility.Awaitility;
import org.ezcode.codetest.application.notification.enums.NotificationType;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.infrastructure.notification.model.NotificationProcessLog;
import org.ezcode.codetest.infrastructure.notification.model.NotificationProcessLog.ProcessStatus;
import org.ezcode.codetest.infrastructure.notification.repository.NotificationMongoRepository;
import org.ezcode.codetest.infrastructure.notification.repository.NotificationProcessLogRepository;
import org.ezcode.codetest.infrastructure.notification.service.NotificationRetryScheduler;
import org.ezcode.codetest.infrastructure.notification.service.NotificationService;
import org.ezcode.codetest.infrastructure.notification.service.ProcessLogService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Disabled
@SpringBootTest
@ActiveProfiles("test")
public class NotificationIntegrationTest {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private NotificationMongoRepository notificationRepository;

	@Autowired
	private NotificationProcessLogRepository processLogRepository;

	@Autowired
	private NotificationRetryScheduler retryScheduler;

	// 실제 객체를 사용하되, 특정 메서드의 동작을 조작하기 위해 @SpyBean 사용
	@MockitoSpyBean
	private NotificationService notificationService;

	@MockitoSpyBean
	private ProcessLogService processLogService;

	@AfterEach
	void tearDown() {
		notificationRepository.deleteAll();
		processLogRepository.deleteAll();
	}

	@Test
	@DisplayName("1. 정상 시나리오: 메시지가 성공적으로 처리되고 상태가 SUCCESS로 기록된다")
	void happyPath_shouldProcessMessageSuccessfully() {
		// Given: 정상 메시지 준비
		String messageId = "ID-" + UUID.randomUUID();
		String payload = createDummyPayload();

		// When: 메시지를 JMS 큐로 전송
		sendMessageToQueue(messageId, payload);

		// Then: 비동기 처리가 완료될 때까지 대기 후 검증
		Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
			// 1. 처리 상태 로그가 SUCCESS로 저장되었는지 확인
			Optional<NotificationProcessLog> logOpt = processLogRepository.findById(messageId);
			assertThat(logOpt).isPresent();
			assertThat(logOpt.get().getStatus()).isEqualTo(ProcessStatus.SUCCESS);

			// 2. 실제 알림 데이터가 DB에 저장되었는지 확인
			assertThat(notificationRepository.count()).isEqualTo(1);
		});
	}

	@Test
	@DisplayName("2. 자동 복구 시나리오: 첫 시도 실패 후, 스케줄러에 의해 재처리되어 성공한다")
	void whenTransientErrorOccurs_shouldBeRecoveredByScheduler() {
		// Given: 메시지 준비 및 첫 시도에만 DB 장애가 발생하도록 설정
		String messageId = "ID-" + UUID.randomUUID();
		String payload = createDummyPayload();

		// 첫 번째 processCreationEvent 호출 시에만 예외를 던지고, 그 이후에는 실제 메서드를 호출하도록 설정
		doThrow(new DataAccessResourceFailureException("DB is temporarily down"))
			.doCallRealMethod()
			.when(notificationService).createNewNotification(any(NotificationCreateEvent.class));

		// When: 1. 첫 번째 메시지 전송 (실패 유도)
		sendMessageToQueue(messageId, payload);

		// Then: 1. 상태가 FAILED로 기록될 때까지 대기
		Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
			Optional<NotificationProcessLog> logOpt = processLogRepository.findById(messageId);
			assertThat(logOpt).isPresent();
			assertThat(logOpt.get().getStatus()).isEqualTo(ProcessStatus.FAILED);
			// 아직 성공 전이므로 알림 데이터는 없어야 함
			assertThat(notificationRepository.count()).isZero();
		});

		// When: 2. 재처리 스케줄러 수동 실행
		System.out.println("--- 재처리 스케줄러 실행 ---");
		retryScheduler.retryFailedNotifications();

		// Then: 2. 재처리가 성공하여 상태가 SUCCESS로 변경될 때까지 대기
		Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
			Optional<NotificationProcessLog> logOpt = processLogRepository.findById(messageId);
			assertThat(logOpt).isPresent();
			assertThat(logOpt.get().getStatus()).isEqualTo(ProcessStatus.SUCCESS);
			// 최종적으로 알림 데이터가 저장되었는지 확인
			assertThat(notificationRepository.count()).isEqualTo(1);
		});
	}

	@Test
	@DisplayName("3. 멱등성 시나리오: 성공한 메시지를 다시 보내도 중복 처리되지 않는다")
	void whenSameMessageIsSent_shouldNotBeProcessedAgain() {
		// Given: 메시지를 한 번 성공적으로 처리
		String messageId = "ID-" + UUID.randomUUID();
		String payload = createDummyPayload();
		sendMessageToQueue(messageId, payload);
		Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() ->
			assertThat(processLogRepository.findById(messageId).get().getStatus()).isEqualTo(ProcessStatus.SUCCESS)
		);
		// UseCase가 1번 호출되었는지 확인
		verify(notificationService, times(1)).createNewNotification(any(NotificationCreateEvent.class));

		// When: 동일한 메시지를 다시 전송
		System.out.println("--- 동일 메시지 재전송 ---");
		sendMessageToQueue(messageId, payload);

		// 잠시 대기 (소비자가 메시지를 처리할 시간)
		try { Thread.sleep(2000); } catch (InterruptedException e) {}

		// Then: UseCase가 추가로 호출되지 않았어야 함 (총 호출 횟수가 여전히 1이어야 함)
		verify(notificationService, times(1)).createNewNotification(any(NotificationCreateEvent.class));
		// 최종 데이터도 1개여야 함
		assertThat(notificationRepository.count()).isEqualTo(1);
	}

	private void sendMessageToQueue(String messageId, String payload) {
		jmsTemplate.convertAndSend(NOTIFICATION_QUEUE_CREATE, payload, message -> {
			message.setStringProperty(CUSTOM_HEADER_MESSAGE_ID, messageId);
			return message;
		});
	}

	private String createDummyPayload() {
		try {
			return objectMapper.writeValueAsString(
					NotificationCreateEvent.of(
					"test@test.com",
					NotificationType.COMMUNITY_DISCUSSION_VOTED_UP,
					null
				)
			);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
