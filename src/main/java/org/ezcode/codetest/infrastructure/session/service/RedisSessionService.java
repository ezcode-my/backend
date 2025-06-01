package org.ezcode.codetest.infrastructure.session.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.ezcode.codetest.application.chatting.port.session.SessionService;
import org.ezcode.codetest.domain.chat.exception.ChattingException;
import org.ezcode.codetest.domain.chat.exception.ChattingExceptionCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisSessionService implements SessionService {

	private final RedisTemplate<String, Long> redisTemplate;
	private static final String CHATROOM_KEY_PREFIX = "chatroom:";
	private static final String SESSION_KEY_PREFIX = "session:";
	private static final String ROOM_COUNT_KEY_PREFIX = "roomCount:";

	private final RedisScript<Long> addSessionScript = new DefaultRedisScript<>(
		"local roomId = ARGV[2]\n" +
			"redis.call('SET', KEYS[2], roomId)\n" +
			"local added = redis.call('SADD', KEYS[1], ARGV[1])\n" +
			"if added == 1 then\n" +
			"  redis.call('INCR', KEYS[3])\n" +
			"end\n" +
			"local cnt = redis.call('GET', KEYS[3])\n" +
			"return tonumber(cnt)\n",
		Long.class
	);

	private final RedisScript<List<Long>> removeSessionScript = new DefaultRedisScript<>(
		"local roomId = redis.call('GET', KEYS[1])\n" +
			"if not roomId then\n" +
			"  return {-1, 0}\n" +
			"end\n" +
			"local roomKey = 'chatroom:' .. roomId\n" +
			"local roomCountKey = 'roomCount:' .. roomId\n" +
			"local removed = redis.call('SREM', roomKey, ARGV[1])\n" +
			"if removed == 1 then\n" +
			"  redis.call('DECR', roomCountKey)\n" +
			"end\n" +
			"redis.call('DEL', KEYS[1])\n" +
			"local cnt = redis.call('GET', roomCountKey)\n" +
			"if not cnt then\n" +
			"  cnt = \"0\"\n" +
			"end\n" +
			"return { tonumber(roomId), tonumber(cnt) }\n",
		(Class<List<Long>>)(Class<?>)List.class
	);

	@Override
	public Long addSession(String sessionId, Long roomId) {

		String roomKey = CHATROOM_KEY_PREFIX + roomId;
		String sessionKey = SESSION_KEY_PREFIX + sessionId;
		String roomCountKey = ROOM_COUNT_KEY_PREFIX + roomId;

		List<String> keys = Arrays.asList(roomKey, sessionKey, roomCountKey);
		Object[] args = new Object[] {sessionId, roomId.toString()};

		Long headCount = redisTemplate.execute(
			addSessionScript,
			keys,
			args
		);
		return (headCount != null ? headCount : 0L);
	}

	@Override
	public Map<String, Long> removeSession(String sessionId) {

		String sessionKey = SESSION_KEY_PREFIX + sessionId;

		List<Long> result = redisTemplate.execute(
			removeSessionScript,
			Collections.singletonList(sessionKey),
			sessionId
		);

		if (result == null || result.size() < 2) {
			throw new ChattingException(ChattingExceptionCode.INVALID_CHATTING_SESSION);
		}

		Long roomId = result.get(0);
		Long headCount = result.get(1);

		if (Objects.equals(roomId, -1L)) {
			throw new ChattingException(ChattingExceptionCode.INVALID_CHATTING_SESSION);
		}

		Map<String, Long> roomData = new HashMap<>();
		roomData.put("roomId", roomId);
		roomData.put("headCount", headCount);

		return roomData;
	}

	@Override
	public Long viewSession(Long roomId) {

		String roomCountKey = ROOM_COUNT_KEY_PREFIX + roomId;

		Long count = redisTemplate.opsForValue().get(roomCountKey);
		return (count != null ? count : 0L);
	}
}

