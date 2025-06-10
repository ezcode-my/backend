package org.ezcode.codetest.application.notification.event;

public record NotificationReadEvent(

	String principalName,

	String notificationId

) {
}
