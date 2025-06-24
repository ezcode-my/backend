package org.ezcode.codetest.infrastructure.persistence.repository.chat;

import java.time.LocalDateTime;
import java.util.List;

import org.ezcode.codetest.domain.chat.repository.ChatRepository;
import org.ezcode.codetest.domain.chat.model.Chat;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {

	private final ChatJpaRepository chatRepository;

	@Override
	public Chat save(Chat chat) {

		return chatRepository.save(chat);
	}

	@Override
	public List<Chat> findChatsFromLastHour(Long roomId) {

		LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

		return chatRepository.findByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(
			roomId,
			oneHourAgo
		);
	}
}
