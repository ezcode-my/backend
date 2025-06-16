package org.ezcode.codetest.infrastructure.persistence.repository.community.vote;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.entity.DiscussionVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionVoteJpaRepository extends JpaRepository<DiscussionVote, Long> {

	Optional<DiscussionVote> findByVoterIdAndDiscussionId(Long voterId, Long discussionId);

	boolean existsByVoterIdAndDiscussionId(Long voterId, Long discussionId);

}
