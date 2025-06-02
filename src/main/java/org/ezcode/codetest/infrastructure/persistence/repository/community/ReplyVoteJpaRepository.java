package org.ezcode.codetest.infrastructure.persistence.repository.community;

import org.ezcode.codetest.domain.community.model.ReplyVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyVoteJpaRepository extends JpaRepository<ReplyVote, Long> {
}
