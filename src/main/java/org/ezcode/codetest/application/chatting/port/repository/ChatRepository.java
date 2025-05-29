package org.ezcode.codetest.application.chatting.port.repository;

import org.ezcode.codetest.domain.chat.model.Chat;

public interface ChatRepository {

	/**
 * 채팅 엔티티를 저장하고 저장된 결과를 반환합니다.
 *
 * @param chat 저장할 채팅 객체
 * @return 저장된 채팅 객체
 */
Chat save(Chat chat);

	/**
 * 주어진 ID로 채팅을 조회하며, 존재하지 않을 경우 예외를 발생시킵니다.
 *
 * @param id 조회할 채팅의 고유 식별자
 * @return 해당 ID에 해당하는 Chat 객체
 * @throws NoSuchElementException 채팅이 존재하지 않을 경우 발생
 */
Chat findOrElseThrow(Long id);

}
