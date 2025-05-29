package org.ezcode.codetest.infrastructure.persitence.repository.chat;

import org.ezcode.codetest.domain.chat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatJpaRepository extends JpaRepository<Chat, Long> {

}
