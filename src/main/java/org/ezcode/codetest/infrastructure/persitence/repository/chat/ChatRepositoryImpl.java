package org.ezcode.codetest.infrastructure.persitence.repository.chat;

import java.time.LocalDateTime;
import java.util.List;

import org.ezcode.codetest.domain.chat.exception.ChattingException;
import org.ezcode.codetest.domain.chat.exception.ChattingExceptionCode;
import org.ezcode.codetest.domain.chat.repository.ChatRepository;
import org.ezcode.codetest.domain.chat.model.Chat;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {

	private final ChatJpaRepository chatRepository;

	public Chat save(Chat chat) {
		return chatRepository.save(chat);
	}

	public Chat findOrElseThrow(Long id) {

		return chatRepository.findById(id).orElseThrow(() ->
			new ChattingException(ChattingExceptionCode.CHATTING_NOT_FOUND));
	}

	public List<Chat> findAll() {

		return chatRepository.findAll();
	}

	public List<Chat> findChatsFromLastHour(Long roomId) {

		LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

		return chatRepository.findByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(
			roomId,
			oneHourAgo
			);
	}
}
