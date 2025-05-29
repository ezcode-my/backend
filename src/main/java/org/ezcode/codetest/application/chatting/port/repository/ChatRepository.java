package org.ezcode.codetest.application.chatting.port.repository;

import org.ezcode.codetest.domain.chat.model.Chat;

public interface ChatRepository {

	Chat save(Chat chat);

	Chat findOrElseThrow(Long id);

}
