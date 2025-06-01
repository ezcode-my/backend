package org.ezcode.codetest.infrastructure.persitence.repository.chat;

import java.util.List;

import org.ezcode.codetest.domain.chat.exception.ChattingException;
import org.ezcode.codetest.domain.chat.exception.ChattingExceptionCode;
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

	public ChatRoom findOrElseThrow(Long id) {

		return chatRoomRepository.findById(id).orElseThrow(() ->
			new ChattingException(ChattingExceptionCode.CHATTING_ROOM_NOT_FOUND));
	}

	public List<ChatRoom> findAll() {

		return chatRoomRepository.findAll();
	}

}
