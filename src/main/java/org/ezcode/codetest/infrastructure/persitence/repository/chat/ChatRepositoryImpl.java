package org.ezcode.codetest.infrastructure.persitence.repository.chat;

import org.ezcode.codetest.application.chatting.port.repository.ChatRepository;
import org.ezcode.codetest.application.chatting.port.repository.ChatRoomRepository;
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
			new RuntimeException("해당 Entity를 찾을 수 없습니다. id = " + id));
		// TODO: 프로젝트에서 사용하는 타입의 exception과 status를 던져줘야 함
	}

}
