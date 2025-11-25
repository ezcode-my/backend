package org.ezcode.codetest.infrastructure.notification.repository;

import java.util.List;

import org.ezcode.codetest.infrastructure.notification.model.NotificationProcessLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationProcessLogRepository extends MongoRepository<NotificationProcessLog, String> {

	// 재시도할 작업들을 찾는 쿼리 메서드
	List<NotificationProcessLog> findByStatusAndRetryCountLessThan(NotificationProcessLog.ProcessStatus status, int maxRetries);
}
