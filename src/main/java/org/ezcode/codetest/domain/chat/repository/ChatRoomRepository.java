package org.ezcode.codetest.domain.chat.repository;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.chat.model.ChatRoom;

public interface ChatRoomRepository {

	ChatRoom save(ChatRoom room);

	void delete(ChatRoom room);

	Optional<ChatRoom> findChatRoom(Long id);

	List<ChatRoom> findAll();

}



