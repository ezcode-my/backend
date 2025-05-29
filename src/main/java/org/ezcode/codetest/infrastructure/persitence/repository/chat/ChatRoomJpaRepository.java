package org.ezcode.codetest.infrastructure.persitence.repository.chat;

import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
}
