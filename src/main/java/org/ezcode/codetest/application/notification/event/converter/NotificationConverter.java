package org.ezcode.codetest.application.notification.event.converter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.mapper.NotificationMapper;
import org.springframework.stereotype.Component;

@Component
public class NotificationConverter {

	private final Map<Class<?>, NotificationMapper<?>> mapperMap;

	public NotificationConverter(List<NotificationMapper<?>> mappers) {
		this.mapperMap = mappers.stream()
			.collect(Collectors.toUnmodifiableMap(
				NotificationMapper::getSupportedType,
				Function.identity()
			));
	}

	@SuppressWarnings("unchecked")
	public NotificationCreateEvent convert(Object event) {
		NotificationMapper mapper = mapperMap.get(event.getClass());
		if (mapper == null) {
			throw new IllegalArgumentException("No mapper found for event type: " + event.getClass());
		}

		return mapper.map(event);
	}
}
