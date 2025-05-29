package org.ezcode.codetest.application.chatting.port.repository;

import org.ezcode.codetest.domain.chat.model.ChatRoom;

public interface ChatRoomRepository {

	/****
 * 주어진 채팅방 엔티티를 저장하거나 갱신합니다.
 *
 * @param chatRoom 저장 또는 갱신할 채팅방 엔티티
 * @return 저장된 채팅방 엔티티
 */
ChatRoom save(ChatRoom chatRoom);

	/**
 * 주어진 ID로 채팅방을 조회하며, 존재하지 않을 경우 예외를 발생시킵니다.
 *
 * @param id 조회할 채팅방의 고유 식별자
 * @return 해당 ID에 해당하는 ChatRoom 객체
 */
ChatRoom findOrElseThrow(Long id);

}



