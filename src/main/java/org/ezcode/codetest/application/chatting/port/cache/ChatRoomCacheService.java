package org.ezcode.codetest.application.chatting.port.cache;

import java.util.List;

public interface ChatRoomCacheService {

	List<ChatRoomCache> getChatRoomsFromCache();

	void addChatRoomToCache(ChatRoomCache room);

	void addChatRoomsToCache(List<ChatRoomCache> rooms);

	void removeChatRoomFromCache(ChatRoomCache room);
}
