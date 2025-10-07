package org.ezcode.codetest.infrastructure.notification.event;

import org.ezcode.codetest.infrastructure.notification.dto.NotificationResponse;

public record NotificationSavedEvent(

	String principalName,

	NotificationResponse response

) {
}
