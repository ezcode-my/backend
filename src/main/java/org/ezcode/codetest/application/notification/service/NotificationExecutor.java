package org.ezcode.codetest.application.notification.service;

import java.util.List;
import java.util.function.Supplier;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.port.NotificationEventService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationExecutor {

	private final NotificationEventService notificationEventService;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void execute(Supplier<List<NotificationCreateEvent>> notificationsEventSupplier) {
		
		try {
			List<NotificationCreateEvent> events = notificationsEventSupplier.get();
			
			if (events != null && !events.isEmpty()) {
				for (NotificationCreateEvent event : events) {
					notificationEventService.notify(event);
				}
				log.info("알림 이벤트 {}개 발행 성공", events.size());
			}
		} catch (Exception ex) {
			log.error("알림 이벤트 발행 실패: {}", ex.getMessage(), ex);
		}
	}
}
