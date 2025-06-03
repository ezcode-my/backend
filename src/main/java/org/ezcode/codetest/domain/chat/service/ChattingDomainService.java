package org.ezcode.codetest.domain.chat.service;

import java.util.List;

import org.ezcode.codetest.domain.chat.exception.ChattingException;
import org.ezcode.codetest.domain.chat.exception.ChattingExceptionCode;
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

	public void removeChatRoom(ChatRoom room) {

		chatRoomRepository.delete(room);
	}

	public void isChatRoomOwner(ChatRoom room, Long userId) {

		if(!room.isOwner(userId)) {
			throw new ChattingException(ChattingExceptionCode.CHATROOM_NOT_OWNER);
		}
	}

	public List<ChatRoom> getChatRoomList() {

		return chatRoomRepository.findAll();
	}

	public ChatRoom getChatRoom(Long roomId) {

		return chatRoomRepository.findChatRoom(roomId).orElseThrow(() ->
			new ChattingException(ChattingExceptionCode.CHATTING_ROOM_NOT_FOUND));
	}

	public Chat createChatting(Chat chat) {

		return chatRepository.save(chat);
	}

	public List<Chat> getRoomChatting(Long roomId) {

		return chatRepository.findChatsFromLastHour(roomId);
	}
}
