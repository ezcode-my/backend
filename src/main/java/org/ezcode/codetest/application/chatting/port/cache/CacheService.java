package org.ezcode.codetest.application.chatting.port.cache;

import java.util.List;

import org.ezcode.codetest.domain.chat.model.ChatRoom;

public interface CacheService {

	List<ChatRoom> getChatRoomsFromCache();

	void replaceChatRoomsCache(List<ChatRoom> rooms);
}
