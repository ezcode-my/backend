package org.ezcode.codetest.infrastructure.persitence.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
}
