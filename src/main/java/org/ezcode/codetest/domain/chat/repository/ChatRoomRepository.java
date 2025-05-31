package org.ezcode.codetest.domain.chat.repository;

import java.util.List;

import org.ezcode.codetest.domain.chat.model.ChatRoom;

public interface ChatRoomRepository {

	void save(ChatRoom chatRoom);

	ChatRoom findOrElseThrow(Long id);

	List<ChatRoom> findAll();

}



