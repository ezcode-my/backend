package org.ezcode.codetest.infrastructure.notification.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.NotificationListRequestEvent;
import org.ezcode.codetest.application.notification.event.NotificationMarkReadEvent;
import org.ezcode.codetest.application.notification.exception.NotificationException;
import org.ezcode.codetest.application.notification.exception.NotificationExceptionCode;
import org.ezcode.codetest.infrastructure.notification.dto.NotificationPageResponse;
import org.ezcode.codetest.infrastructure.notification.dto.NotificationResponse;
import org.ezcode.codetest.infrastructure.notification.model.NotificationDocument;
import org.ezcode.codetest.infrastructure.notification.repository.NotificationMongoRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	private final NotificationMongoRepository mongoRepository;

	private final RedisTemplate<String, Object> redisTemplate;

	public NotificationService(
		NotificationMongoRepository mongoRepository,
		@Qualifier("notificationRedisTemplate") RedisTemplate<String, Object> redisTemplate
	) {
		this.mongoRepository = mongoRepository;
		this.redisTemplate = redisTemplate;
	}

	public NotificationDocument createNewNotification(NotificationCreateEvent event) {

		NotificationDocument document = mongoRepository.save(NotificationDocument.from(event));

		evictNotificationListCache(event.principalName());

		return document;
	}

	@Cacheable(value = "notificationList", key = "#event.principalName + ':' + #event.page + ':' + #event.size", cacheManager = "notificationRedisCacheManager")
	public NotificationPageResponse<NotificationResponse> getNotifications(NotificationListRequestEvent event) {

		PageRequest pageRequest = PageRequest.of(event.page(), event.size(), Sort.by("createdAt").descending());

		Page<NotificationDocument> page = mongoRepository.findAllByPrincipalName(event.principalName(), pageRequest);

		List<NotificationResponse> notificationList = page.stream()
			.map(NotificationResponse::from)
			.toList();

		return new NotificationPageResponse<>(
			notificationList,
			page.getNumber(),
			page.getSize(),
			page.getTotalElements()
		);
	}

	public void markAsRead(NotificationMarkReadEvent event) {

		NotificationDocument notificationDocument = mongoRepository
			.findByIdAndPrincipalName(event.notificationId(), event.principalName())
			.orElseThrow(() -> new NotificationException(NotificationExceptionCode.NOTIFICATION_NOT_FOUND));

		notificationDocument.markAsRead();
		mongoRepository.save(notificationDocument);

		evictNotificationListCache(event.principalName());
	}

	private void evictNotificationListCache(String principalName) {

		String pattern = "notificationList::" + principalName.replaceAll("[*?\\[\\]]", "\\\\$0") + ":*";

		Set<String> keys = redisTemplate.execute((RedisCallback<Set<String>>)connection -> {
			Set<String> matchingKeys = new HashSet<>();
			ScanOptions options = ScanOptions.scanOptions().match(pattern).count(1000).build();
			Cursor<byte[]> cursor = connection.scan(options);
			while (cursor.hasNext()) {
				matchingKeys.add(new String(cursor.next()));
			}
			return matchingKeys;
		});

		if (keys != null && !keys.isEmpty()) {
			redisTemplate.delete(keys);
		}
	}
}
