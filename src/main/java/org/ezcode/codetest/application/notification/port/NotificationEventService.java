package org.ezcode.codetest.application.notification.port;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.NotificationListRequestEvent;
import org.ezcode.codetest.application.notification.event.NotificationReadEvent;

public interface NotificationEventService {

	void saveAndNotify(NotificationCreateEvent dto);

	void notifyList(NotificationListRequestEvent dto);

	void setRead(NotificationReadEvent dto);

}
