package org.ezcode.codetest.infrastructure.persistence.repository.notification;

import org.ezcode.codetest.domain.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
}
