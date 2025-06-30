package org.ezcode.codetest.application.notification.event;

public record NotificationMarkReadEvent(

	String principalName,

	String notificationId

) {
}
