package org.ezcode.codetest.infrastructure.cache.service;

import java.util.List;

import org.ezcode.codetest.application.chatting.port.cache.ChatRoomCacheService;
import org.ezcode.codetest.application.chatting.port.cache.ChatRoomCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisCacheService implements ChatRoomCacheService {

	private final RedisTemplate<String, ChatRoomCache> redisTemplate;
	private static final String CHAT_ROOM_LIST = "ChatRoomList";

	public List<ChatRoomCache> getChatRoomsFromCache() {

		return redisTemplate
			.opsForSet()
			.members(CHAT_ROOM_LIST)
			.stream()
			.toList();
	}

	public void addChatRoomToCache(ChatRoomCache room) {

		redisTemplate.opsForSet().add(CHAT_ROOM_LIST, room);
	}

	public void addChatRoomsToCache(List<ChatRoomCache> rooms) {

		redisTemplate.opsForSet().add(CHAT_ROOM_LIST, rooms.toArray(new ChatRoomCache[0]));
	}

	public void removeChatRoomFromCache(ChatRoomCache room) {

		redisTemplate.opsForSet().remove(CHAT_ROOM_LIST, room);
	}
}
