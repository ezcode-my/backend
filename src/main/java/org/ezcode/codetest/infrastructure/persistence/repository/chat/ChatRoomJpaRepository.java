package org.ezcode.codetest.infrastructure.persistence.repository.chat;

import java.util.List;

import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.lang.NonNull;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {

	@EntityGraph(attributePaths = "user")
	@NonNull
	List<ChatRoom> findAll();
}
