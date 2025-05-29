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

	/**
	 * Chat 엔티티를 저장하고 저장된 엔티티를 반환합니다.
	 *
	 * @param chat 저장할 Chat 엔티티
	 * @return 저장된 Chat 엔티티
	 */
	public Chat save(Chat chat) {
		return chatRepository.save(chat);
	}

	/**
	 * 주어진 ID로 Chat 엔티티를 조회하며, 존재하지 않을 경우 예외를 발생시킵니다.
	 *
	 * @param id 조회할 Chat 엔티티의 ID
	 * @return 조회된 Chat 엔티티
	 * @throws RuntimeException 해당 ID의 Chat 엔티티가 존재하지 않을 경우 발생
	 */
	public Chat findOrElseThrow(Long id) {

		return chatRepository.findById(id).orElseThrow(() ->
			new RuntimeException("해당 Entity를 찾을 수 없습니다. id = " + id));
		// TODO: 프로젝트에서 사용하는 타입의 exception과 status를 던져줘야 함
	}

}
