package org.ezcode.codetest.infrastructure.notification;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Duration;

import org.ezcode.codetest.application.notification.enums.NotificationType;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.exception.NotificationException;
import org.ezcode.codetest.infrastructure.notification.model.NotificationDocument;
import org.ezcode.codetest.infrastructure.notification.repository.NotificationMongoRepository;
import org.ezcode.codetest.infrastructure.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@SpringBootTest(properties = {
	// 테스트 실행 속도를 위해 서킷 대기 시간을 짧게 조정
	"resilience4j.circuitbreaker.instances.db-circuit.wait-duration-in-open-state=2s"
})
@ActiveProfiles("test")
public class NotificationCircuitBreakTest {

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private CircuitBreakerRegistry circuitBreakerRegistry;
	
	@MockitoBean
	private NotificationMongoRepository mongoRepository;
	
	private CircuitBreaker dbCircuitBreaker;

	@BeforeEach
	void setUp() {
		// 테스트 시작 전에 서킷 브레이커를 초기화 (CLOSED 상태로 강제)
		dbCircuitBreaker = circuitBreakerRegistry.circuitBreaker("db-circuit");
		dbCircuitBreaker.reset();
	}

	@Test
	@DisplayName("1. 정상 상황: DB가 안정적일 때 서킷은 CLOSED 상태를 유지한다")
	void whenDbIsStable_thenCircuitRemainsClosed() {
		// Given
		NotificationCreateEvent event = createNotificationCreateEvent();
		NotificationDocument dummyDocument = NotificationDocument.from(event);

		when(mongoRepository.save(any(NotificationDocument.class))).thenReturn(dummyDocument);

		// When & Then
		for (int i = 0; i < 10; i++) {
			assertDoesNotThrow(() -> notificationService.createNewNotification(createNotificationCreateEvent()));
		}
		assertThat(dbCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
		System.out.println("✅ 성공: 정상 상황 테스트 완료");
	}

	@Test
	@DisplayName("2. 장애 발생: DB 장애 반복 시 서킷이 OPEN 상태로 변경된다")
	void whenDbFailureRepeats_thenCircuitOpens() {
		// Given
		String errorMessage = "DB Connection Failed";
		when(mongoRepository.save(any(NotificationDocument.class)))
			.thenThrow(new DataAccessResourceFailureException(errorMessage));

		// When & Then
		for (int i = 0; i < 5; i++) {
			assertThatThrownBy(() -> notificationService.createNewNotification(createNotificationCreateEvent()))
				.isInstanceOf(NotificationException.class)
				.hasCauseInstanceOf(DataAccessResourceFailureException.class);
		}

		assertThat(dbCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
		System.out.println("✅ 성공: 장애 발생 시 OPEN 전환 테스트 완료");
	}

	@Test
	@DisplayName("3. 자동 복구: 서킷이 열린 후, 시간이 지나면 HALF_OPEN을 거쳐 CLOSED로 복구된다")
	void whenCircuitIsOpen_andAfterWaitDuration_thenTransitionsToHalfOpenAndClosed() throws InterruptedException {
		// Given
		NotificationCreateEvent event = createNotificationCreateEvent();
		NotificationDocument dummyDocument = NotificationDocument.from(event);

		dbCircuitBreaker.transitionToOpenState();
		Thread.sleep(Duration.ofSeconds(2).toMillis());
		when(mongoRepository.save(any(NotificationDocument.class))).thenReturn(dummyDocument);

		// When
		for (int i = 0; i < 5; i++) {
			notificationService.createNewNotification(createNotificationCreateEvent());
		}

		// Then
		assertThat(dbCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
		System.out.println("✅ 성공: 자동 복구 테스트 완료");
	}

	@Test
	@DisplayName("4. 복구 실패: HALF_OPEN 상태에서 호출이 실패하면 다시 OPEN 상태로 돌아간다")
	void whenCircuitIsHalfOpen_andCallFails_thenTransitionsBackToOpen() throws InterruptedException {
		// Given
		String errorMessage = "DB Still Down";
		int minimumCalls = 5;

		// 1. 서킷을 OPEN 상태로 만들고, HALF_OPEN이 될 때까지 대기
		dbCircuitBreaker.transitionToOpenState();
		Thread.sleep(Duration.ofSeconds(2).toMillis());

		// 2. HALF_OPEN 상태에서 DB가 여전히 장애 상황임을 흉내
		when(mongoRepository.save(any(NotificationDocument.class)))
			.thenThrow(new DataAccessResourceFailureException(errorMessage));

		// When: 최소 호출 횟수만큼 실패를 유도
		for (int i = 0; i < minimumCalls; i++) {
			assertThatThrownBy(() -> notificationService.createNewNotification(createNotificationCreateEvent()))
				.isInstanceOf(NotificationException.class);
		}

		// Then: 이제 충분한 실패 데이터가 쌓였으므로 서킷은 다시 OPEN 상태로 돌아가야 함
		assertThat(dbCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
		System.out.println("✅ 성공: 복구 실패 테스트 완료");
	}

	@Test
	@DisplayName("5. 서킷이 OPEN일 때 Fallback 동작 검증")
	void whenCircuitIsOpen_thenFallbackIsExecuted() {
		// Given
		dbCircuitBreaker.transitionToOpenState();

		// When & Then
		assertThatThrownBy(() -> notificationService.createNewNotification(createNotificationCreateEvent()))
			.isInstanceOf(NotificationException.class)
			.hasCauseInstanceOf(CallNotPermittedException.class);

		// DB Repository는 절대 호출되지 않았어야 함
		verify(mongoRepository, never()).save(any(NotificationDocument.class));
		System.out.println("✅ 성공: OPEN 상태에서 Fallback 동작 검증 완료");
	}


	private NotificationCreateEvent createNotificationCreateEvent() {
		return NotificationCreateEvent.of(
			"test@test.com",
			NotificationType.COMMUNITY_DISCUSSION_VOTED_UP,
			null
		);
	}
}
