package org.ezcode.codetest.infrastructure.persitence.repository.chat;

import org.ezcode.codetest.application.chatting.port.repository.ChatRoomRepository;
import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

	private final ChatRoomJpaRepository chatRoomRepository;

	/**
	 * 주어진 채팅방 엔티티를 저장하고, 저장된 결과를 반환합니다.
	 *
	 * @param chatRoom 저장할 ChatRoom 엔티티
	 * @return 저장된 ChatRoom 엔티티
	 */
	public ChatRoom save(ChatRoom chatRoom) {

		return chatRoomRepository.save(chatRoom);
	}

	/**
	 * 주어진 ID로 채팅방 엔티티를 조회하며, 존재하지 않을 경우 예외를 발생시킵니다.
	 *
	 * @param id 조회할 채팅방의 ID
	 * @return 조회된 ChatRoom 엔티티
	 * @throws RuntimeException 해당 ID의 채팅방이 존재하지 않을 경우 발생
	 */
	public ChatRoom findOrElseThrow(Long id) {

		return chatRoomRepository.findById(id).orElseThrow(() ->
			new RuntimeException("해당 Entity를 찾을 수 없습니다. id = " + id));
		// TODO: 프로젝트에서 사용하는 타입의 exception과 status를 던져줘야 함
	}

}
