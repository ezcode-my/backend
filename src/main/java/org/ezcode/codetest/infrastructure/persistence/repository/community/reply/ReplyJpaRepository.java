package org.ezcode.codetest.infrastructure.persistence.repository.community.reply;

import org.ezcode.codetest.domain.community.model.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long>, ReplyQueryRepository {
}
