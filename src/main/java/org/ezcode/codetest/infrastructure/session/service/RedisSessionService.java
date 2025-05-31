package org.ezcode.codetest.infrastructure.session.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezcode.codetest.application.chatting.port.session.SessionService;
import org.ezcode.codetest.domain.chat.exception.ChattingException;
import org.ezcode.codetest.domain.chat.exception.ChattingExceptionCode;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisSessionService implements SessionService {

	private final StringRedisTemplate redisTemplate;

	private final DefaultRedisScript<Long> addSessionScript = new DefaultRedisScript<>(
		"local roomId = ARGV[2]\n" +
			"redis.call('SET', KEYS[2], roomId)\n" +
			"local added = redis.call('SADD', KEYS[1], ARGV[1])\n" +
			"if added == 1 then\n" +
			"  redis.call('INCR', KEYS[3])\n" +
			"end\n" +
			"local cnt = redis.call('GET', KEYS[3])\n" +
			"return tonumber(cnt)\n"
		, Long.class
	);

	private final DefaultRedisScript<List> removeSessionScript = new DefaultRedisScript<>(
		"local roomId = redis.call('GET', KEYS[1])\n" +
			"if not roomId then\n" +
			"  return {\"-1\", 0}\n" +
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
			"return { roomId, tonumber(cnt) }\n"
		, List.class
	);

	@Override
	public Long addSession(String sessionId, Long roomId) {

		String roomKey = "chatroom:" + roomId;
		String sessionKey = "session:" + sessionId;
		String roomCountKey = "roomCount:" + roomId;

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

 		String sessionKey = "session:" + sessionId;

		List<Object> result = redisTemplate.execute(
			removeSessionScript,
			Collections.singletonList(sessionKey),
			sessionId
		);

		if (result == null) {
			throw new ChattingException(ChattingExceptionCode.INVALID_CHATTING_SESSION);
		}

		Object roomIdObj = result.get(0);
		String roomIdStr = roomIdObj.toString();

		if ("-1".equals(roomIdStr)) {
			throw new ChattingException(ChattingExceptionCode.INVALID_CHATTING_SESSION);
		}

		Long roomId = Long.valueOf(roomIdStr);

		Object cntObj = result.get(1);
		Long headCount = (cntObj instanceof Long)
			? (Long) cntObj
			: Long.valueOf(cntObj.toString());

		Map<String, Long> roomData = new HashMap<>();
		roomData.put("roomId", roomId);
		roomData.put("headCount", headCount);

		return roomData;
	}

	@Override
	public Long viewSession(Long roomId) {

		String roomCountKey = "roomCount:" + roomId;
		String countStr = redisTemplate.opsForValue().get(roomCountKey);
		if (countStr == null) {
			return 0L;
		}
		return Long.valueOf(countStr);
	}
}

