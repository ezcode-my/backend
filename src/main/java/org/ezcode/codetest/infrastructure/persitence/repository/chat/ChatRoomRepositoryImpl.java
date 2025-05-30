package org.ezcode.codetest.infrastructure.persitence.repository.chat;

import java.util.List;

import org.ezcode.codetest.domain.chat.repository.ChatRoomRepository;
import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

	private final ChatRoomJpaRepository chatRoomRepository;

	public void save(ChatRoom chatRoom) {

		chatRoomRepository.save(chatRoom);

	}

	public ChatRoom findOrElseThrow(Long id) {

		return chatRoomRepository.findById(id).orElseThrow(() ->
			new RuntimeException("해당 Entity를 찾을 수 없습니다. id = " + id));
		// TODO: 프로젝트에서 사용하는 타입의 exception과 status를 던져줘야 함

	}

	public List<ChatRoom> findAll() {

		return chatRoomRepository.findAll();

	}

}
