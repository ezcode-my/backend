package org.ezcode.codetest.infrastructure.persitence.repository.community;

import org.ezcode.codetest.domain.community.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {
}
