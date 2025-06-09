package org.ezcode.codetest.infrastructure.persistence.repository.notification;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.ezcode.codetest.infrastructure.event.dto.NotificationRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationRedisRepository implements NotificationRepository {

	private final RedisTemplate<String, NotificationRecord> redisTemplate;

	private static final String KEY_PREFIX = "notifications:%s";

	@Override
	public void save(NotificationRecord record) {

		String key = String.format(KEY_PREFIX, record.getPrincipalName());
		ZSetOperations<String, NotificationRecord> zSetOps = redisTemplate.opsForZSet();

		long score = record.getCreatedAt()
			.atZone(ZoneId.systemDefault())
			.toInstant()
			.toEpochMilli();
		zSetOps.add(key, record, score);
	}

	@Override
	public List<NotificationRecord> findAll(String principalName, int page, int size) {

		String key = String.format(KEY_PREFIX, principalName);
		ZSetOperations<String, NotificationRecord> zSetOps = redisTemplate.opsForZSet();

		int start = (page - 1) * size;
		int end = start + size - 1;
		Set<NotificationRecord> set = zSetOps.reverseRange(key, start, end);
		return (set == null) ? List.of() : new ArrayList<>(set);
	}

	@Override
	public void markAsRead(String principalName, String notificationId) {

		String key = String.format(KEY_PREFIX, principalName);
		ZSetOperations<String, NotificationRecord> zSetOps = redisTemplate.opsForZSet();

		// 전체 스캔 → 개선 필요 시 Hash+ZSet 구조 고려
		Set<NotificationRecord> all = zSetOps.range(key, 0, -1);
		if (all == null)	return;

		for (NotificationRecord rec : all) {
			if (rec.getId().equals(notificationId)) {
				long score = rec.getCreatedAt()
					.atZone(ZoneId.systemDefault())
					.toInstant()
					.toEpochMilli();

				// 기존 데이터 삭제 및 수정한 데이터 삽입
				zSetOps.remove(key, rec);
				rec.setRead();
				zSetOps.add(key, rec, score);

				break;
			}
		}
	}
}
