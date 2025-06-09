package org.ezcode.codetest.application.notification.dto;

public record NotificationListRequestEvent(

	String principalName,

	int page,

	int size

) {
}
