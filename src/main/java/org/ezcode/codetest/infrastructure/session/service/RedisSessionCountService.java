package org.ezcode.codetest.infrastructure.session.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.ezcode.codetest.application.chatting.port.session.ChatSessionService;
import org.ezcode.codetest.application.chatting.port.session.RoomSessionInfo;
import org.ezcode.codetest.domain.chat.exception.ChattingException;
import org.ezcode.codetest.domain.chat.exception.ChattingExceptionCode;
import org.ezcode.codetest.infrastructure.session.constant.RedisKeyConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisSessionCountService implements ChatSessionService {

	private final RedisTemplate<String, Long> redisTemplate;

	private final RedisScript<Long> addSessionScript =
		new DefaultRedisScript<>(
			"""
				local roomId = ARGV[2]
				redis.call('SET', KEYS[2], roomId)
				local added = redis.call('SADD', KEYS[1], ARGV[1])
				if added == 1 then
				  redis.call('INCR', KEYS[3])
				end
				local cnt = redis.call('GET', KEYS[3])
				return tonumber(cnt)
				"""
			, Long.class);

	private final RedisScript<List<Long>> removeSessionScript =
		new DefaultRedisScript<>(
			"""
				local roomId = redis.call('GET', KEYS[1])
				if not roomId then
				  return {-1, 0}
				end
				local roomKey = 'chatroom:' .. roomId
				local roomCountKey = 'roomCount:' .. roomId
				local removed = redis.call('SREM', roomKey, ARGV[1])
				if removed == 1 then
				  redis.call('DECR', roomCountKey)
				end
				redis.call('DEL', KEYS[1])
				local cnt = redis.call('GET', roomCountKey)
				if not cnt then
				  cnt = "0"
				end
				return { tonumber(roomId), tonumber(cnt) }
				"""
			, (Class<List<Long>>)(Class<?>)List.class);

	private final RedisScript<Long> removeRoomScript =
		new DefaultRedisScript<>(
			"""
				local sessions = redis.call('SMEMBERS', KEYS[1])
				for i=1,#sessions do
				  local sessionKey = 'session:' .. sessions[i]
				  redis.call('DEL', sessionKey)
				end
				redis.call('DEL', KEYS[1])
				redis.call('DEL', KEYS[2])
				return #sessions
				"""
			, Long.class);

	@Override
	public Long addSessionCount(String sessionId, Long roomId) {

		String roomKey = RedisKeyConstants.CHATROOM_KEY_PREFIX + roomId;
		String sessionKey = RedisKeyConstants.SESSION_KEY_PREFIX + sessionId;
		String roomCountKey = RedisKeyConstants.ROOM_COUNT_KEY_PREFIX + roomId;

		List<String> keys = Arrays.asList(roomKey, sessionKey, roomCountKey);
		Object[] args = new Object[] {sessionId, roomId.toString()};

		return redisTemplate.execute(
			addSessionScript,
			keys,
			args
		);
	}

	public void removeRoomSession(Long roomId) {

		String roomKey = RedisKeyConstants.CHATROOM_KEY_PREFIX + roomId;
		String roomCountKey = RedisKeyConstants.ROOM_COUNT_KEY_PREFIX + roomId;
		List<String> keys = Arrays.asList(roomKey, roomCountKey);

		redisTemplate.execute(
			removeRoomScript,
			keys,
			new Object[] {}
		);
	}

	@Override
	public RoomSessionInfo removeSessionCount(String sessionId) {

		String sessionKey = RedisKeyConstants.SESSION_KEY_PREFIX + sessionId;

		List<Long> result = redisTemplate.execute(
			removeSessionScript,
			Collections.singletonList(sessionKey),
			sessionId
		);

		if (result.size() < 2) {
			throw new ChattingException(ChattingExceptionCode.INVALID_CHATTING_SESSION);
		}

		Long roomId = result.get(0);
		Long headCount = result.get(1);

		if (Objects.equals(roomId, -1L)) {
			throw new ChattingException(ChattingExceptionCode.INVALID_CHATTING_SESSION);
		}

		return RoomSessionInfo.builder()
			.roomId(roomId)
			.headCount(headCount)
			.build();
	}

	@Override
	public Long viewSessionCount(Long roomId) {

		String roomCountKey = RedisKeyConstants.ROOM_COUNT_KEY_PREFIX + roomId;

		Long count = redisTemplate.opsForValue().get(roomCountKey);
		return (count != null ? count : 0L);
	}
}

