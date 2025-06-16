package org.ezcode.codetest.infrastructure.persistence.repository.community.vote;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.entity.DiscussionVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiscussionVoteJpaRepository extends JpaRepository<DiscussionVote, Long> {

	Optional<DiscussionVote> findByVoterIdAndDiscussionId(Long voterId, Long discussionId);

	boolean existsByVoterIdAndDiscussionId(Long voterId, Long discussionId);

	@Query("SELECT COUNT(dv) FROM DiscussionVote dv WHERE dv.discussion.id = :discussionId AND dv.voteType = 'UP'")
	Long countUpvoteByDiscussionId(Long discussionId);

	@Query("SELECT COUNT(dv) FROM DiscussionVote dv WHERE dv.discussion.id = :discussionId AND dv.voteType = 'DOWN'")
	Long countDownvoteByDiscussionId(Long discussionId);

}
