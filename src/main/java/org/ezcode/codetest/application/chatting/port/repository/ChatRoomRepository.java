package org.ezcode.codetest.application.chatting.port.repository;

import org.ezcode.codetest.domain.chat.model.ChatRoom;

public interface ChatRoomRepository {

	ChatRoom save(ChatRoom chatRoom);

	ChatRoom findOrElseThrow(Long id);

}



