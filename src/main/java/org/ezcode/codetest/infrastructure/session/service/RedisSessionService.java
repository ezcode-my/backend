package org.ezcode.codetest.infrastructure.session.service;

import java.util.HashMap;
import java.util.Map;

import org.ezcode.codetest.application.chatting.port.session.SessionService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisSessionService implements SessionService {

	private final StringRedisTemplate redisTemplate;

	public Long addSession(String sessionId, Long roomId) {

		redisTemplate.opsForSet().add(roomId.toString(), sessionId);
		redisTemplate.opsForValue().set(sessionId, roomId.toString());

		Long countSession = redisTemplate.opsForSet().size(roomId.toString());
		return countSession != null ? countSession : 0;

	}

	public Map<String, Long> removeSession(String sessionId) {

		String roomId = redisTemplate.opsForValue().get(sessionId);

		if (roomId == null) {
			throw new RuntimeException("세션 ID로 RoomID 조회 불가");
		}

		//Redis 에 저장된 세션을 하나 삭제하고 남은 세션 갯수(방 인원수) 를 받음
		redisTemplate.opsForSet().remove(roomId, sessionId);
		Long countSession = redisTemplate.opsForSet().size(roomId);

		//Map 으로 반환
		Map<String, Long> roomData = new HashMap<>();
		roomData.put("roomId", Long.valueOf(roomId));
		roomData.put("headCount", countSession);
		return roomData;
	}

	public Long viewSession(Long roomId) {

		Long countSession = redisTemplate.opsForSet().size(roomId.toString());
		return countSession != null ? countSession : 0;

	}

}
