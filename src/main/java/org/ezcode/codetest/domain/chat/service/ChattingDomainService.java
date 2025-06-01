package org.ezcode.codetest.domain.chat.service;

import java.util.List;

import org.ezcode.codetest.domain.chat.model.Chat;
import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.ezcode.codetest.domain.chat.repository.ChatRepository;
import org.ezcode.codetest.domain.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChattingDomainService {

	private final ChatRepository chatRepository;
	private final ChatRoomRepository chatRoomRepository;

	public ChatRoom createChatRoom(ChatRoom room) {

		return chatRoomRepository.save(room);
	}

	public List<ChatRoom> getChatRoomList() {

		return chatRoomRepository.findAll();
	}

	public ChatRoom getChatRoom(Long roomId) {

		return chatRoomRepository.findOrElseThrow(roomId);
	}

	public Chat createChatting(Chat chat) {

		return chatRepository.save(chat);
	}

	public List<Chat> getRoomChatting(Long roomId) {

		return chatRepository.findChatsFromLastHour(roomId);
	}
}
