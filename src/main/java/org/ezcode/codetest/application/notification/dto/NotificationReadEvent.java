package org.ezcode.codetest.application.notification.dto;

public record NotificationReadEvent(

	String principalName,

	String notificationId

) {
}
