package org.ezcode.codetest.application.notification.event;

public record NotificationListRequestEvent(

	String principalName,

	int page,

	int size

) {
}
