package org.ezcode.codetest.domain.chat.repository;

import java.util.List;

import org.ezcode.codetest.domain.chat.model.Chat;

public interface ChatRepository {

	Chat save(Chat chat);

	List<Chat> findChatsFromLastHour(Long roomId);

}
