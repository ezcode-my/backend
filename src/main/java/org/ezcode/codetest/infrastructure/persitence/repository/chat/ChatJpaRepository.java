package org.ezcode.codetest.infrastructure.persitence.repository.chat;

import java.time.LocalDateTime;
import java.util.List;

import org.ezcode.codetest.domain.chat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatJpaRepository extends JpaRepository<Chat, Long> {

	List<Chat> findAllByChatRoomId(Long id);

	List<Chat> findByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(
		Long chatRoomId,
		LocalDateTime cutoff
	);

}
