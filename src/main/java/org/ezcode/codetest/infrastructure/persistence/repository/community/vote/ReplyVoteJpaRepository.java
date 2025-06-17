package org.ezcode.codetest.infrastructure.persistence.repository.community.vote;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.entity.ReplyVote;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyVoteJpaRepository extends JpaRepository<ReplyVote, Long> {

	Optional<ReplyVote> findByVoterIdAndReplyId(Long voterId, Long replyId);

	boolean existsByVoterIdAndReplyId(Long voterId, Long replyId);

	@Query("SELECT COUNT(rv) FROM ReplyVote rv WHERE rv.reply.id = :replyId AND rv.voteType = :voteType")
	Long countUpvoteByReplyId(Long replyId, VoteType voteType);

	@Query("SELECT COUNT(rv) FROM ReplyVote rv WHERE rv.reply.id = :replyId AND rv.voteType = :voteType")
	Long countDownvoteByReplyId(Long replyId, VoteType voteType);
}
