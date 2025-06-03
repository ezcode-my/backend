package org.ezcode.codetest.infrastructure.persistence.repository.chat;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.chat.repository.ChatRoomRepository;
import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

	private final ChatRoomJpaRepository chatRoomRepository;

	public ChatRoom save(ChatRoom room) {

		return chatRoomRepository.save(room);
	}

	public void delete(ChatRoom room) {

		room.deleteChatRoom();
	}

	public Optional<ChatRoom> findChatRoom(Long id) {

		return chatRoomRepository.findByIdAndIsDeleted(id, false);
	}

	public List<ChatRoom> findAll() {

		return chatRoomRepository.findAllByIsDeleted(false);
	}
}
