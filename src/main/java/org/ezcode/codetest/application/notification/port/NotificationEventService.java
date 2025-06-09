package org.ezcode.codetest.application.notification.port;

import org.ezcode.codetest.application.notification.dto.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.dto.NotificationListRequestEvent;
import org.ezcode.codetest.application.notification.dto.NotificationReadEvent;

public interface NotificationEventService {

	void saveAndNotify(NotificationCreateEvent dto);

	void notifyList(NotificationListRequestEvent dto);

	void setRead(NotificationReadEvent dto);

}
