package org.ezcode.codetest.infrastructure.cache.service;

import java.util.List;

import org.ezcode.codetest.application.chatting.port.cache.CacheService;
import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisCacheService implements CacheService {

	private final RedisTemplate<String, List<ChatRoom>> redisTemplate;
	private static final String CHAT_ROOM_LIST = "ChatRoomList";

	public List<ChatRoom> getChatRoomsFromCache() {

		return redisTemplate.opsForValue().get(CHAT_ROOM_LIST);
	}

	public void replaceChatRoomsCache(List<ChatRoom> rooms) {

		redisTemplate.opsForValue().set(CHAT_ROOM_LIST, rooms);
	}
}
