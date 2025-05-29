package org.ezcode.codetest.application.chatting.service;

import org.ezcode.codetest.application.chatting.port.message.MessageService;
import org.ezcode.codetest.application.chatting.port.repository.ChatRepository;
import org.ezcode.codetest.application.chatting.port.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatRepository chatRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final MessageService messageService;

}
