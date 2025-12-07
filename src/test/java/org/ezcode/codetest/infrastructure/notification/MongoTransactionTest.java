package org.ezcode.codetest.infrastructure.notification;

import org.ezcode.codetest.application.notification.enums.NotificationType;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.infrastructure.notification.model.NotificationDocument;
import org.ezcode.codetest.infrastructure.notification.repository.NotificationMongoRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Disabled
@SpringBootTest
@ActiveProfiles("test")
public class MongoTransactionTest {

	@Autowired
	private NotificationMongoRepository mongoRepository;

	@Test
	@Transactional(transactionManager = "mongoTransactionManager")
	void transactionTest() {
		mongoRepository.save(NotificationDocument.from(
			NotificationCreateEvent.of(
				"test@test.com",
				NotificationType.COMMUNITY_DISCUSSION_VOTED_UP,
				null
			)
		));
	}
}
