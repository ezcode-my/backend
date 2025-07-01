package org.ezcode.codetest.application.notification.port;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.NotificationListRequestEvent;
import org.ezcode.codetest.application.notification.event.NotificationMarkReadEvent;

public interface NotificationEventService {

	void notify(NotificationCreateEvent dto);

	void notifyList(NotificationListRequestEvent dto);

	void setRead(NotificationMarkReadEvent dto);

}
