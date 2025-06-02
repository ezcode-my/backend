package org.ezcode.codetest.infrastructure.persistence.repository.community;

import org.ezcode.codetest.domain.community.model.DiscussionVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionVoteJpaRepository extends JpaRepository<DiscussionVote, Long> {
}
