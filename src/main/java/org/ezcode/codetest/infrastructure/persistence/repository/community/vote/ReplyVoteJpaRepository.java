package org.ezcode.codetest.infrastructure.persistence.repository.community.vote;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.ReplyVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyVoteJpaRepository extends JpaRepository<ReplyVote, Long> {

	Optional<ReplyVote> findByVoterIdAndReplyId(Long voterId, Long replyId);

	boolean existsByVoterIdAndReplyId(Long voterId, Long replyId);

}
